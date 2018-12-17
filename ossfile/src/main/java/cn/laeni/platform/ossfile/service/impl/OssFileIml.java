package cn.laeni.platform.ossfile.service.impl;

import cn.laeni.platform.ossfile.entity.*;
import cn.laeni.platform.ossfile.entity.constant.OssCodeEnum;
import cn.laeni.platform.ossfile.entity.constant.SessionKeyEnum;
import cn.laeni.platform.ossfile.entity.constant.SystemCodeEnum;
import cn.laeni.platform.ossfile.entity.other.ApiJson;
import cn.laeni.platform.ossfile.entity.other.Oss;
import cn.laeni.platform.ossfile.entity.other.OssResult;
import cn.laeni.platform.ossfile.mapper.*;
import cn.laeni.platform.ossfile.service.OssFileService;
import cn.laeni.platform.ossfile.tool.FileTool;
import cn.laeni.platform.ossfile.tool.RandomNumber;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

/**
 * 上传文件
 *
 * @author laeni.cn
 */
@Service
@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
public class OssFileIml implements OssFileService {

    protected final Log logger = LogFactory.getLog(getClass());

    @Value("${maxFileSize:10485760}")
    private Long maxFileSize;

    /**
     * 对象存储
     */
    @Autowired
    Oss oss;

    /**
     * FilesMapper
     */
    @Autowired
    PathAndFileMapper pathAndFileMapper;

    /**
     * FilesMapper
     */
    @Autowired
    FilesMapper filesMapper;

    /**
     * PathStructureMapper
     */
    @Autowired
    PathStructureMapper pathStructureMapper;

    /**
     * FileLinkMapper
     */
    @Autowired
    FileLinkMapper fileLinkMapper;

    /**
     * UserSpace
     */
    @Autowired
    UserSpaceMapper userSpaceMapper;

    /**
     * 上传文件到对象存储
     *
     * @param upFile 待上传的文件
     * @param userId    userId
     * @param oss    对象存储Bean
     * @return
     */
    @Override
    public OssResult localFile(MultipartFile upFile, String userId, Oss oss) {
        OssResult ossResult = new OssResult();
        // 设置文件的原始文件名
        ossResult.setOriginalName(upFile.getOriginalFilename());
        // 设置文件大小(单位字节)
        ossResult.setSize(upFile.getSize());
        // 设置文件后缀名
        ossResult.setSuffix(FileTool.getFileType(upFile.getOriginalFilename()));
        // 设置文件key
        Calendar calendar = Calendar.getInstance();
        ossResult.setKey(
                new StringBuilder()
                        // 前缀: 年/月/
                        .append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH) + 1).append("/")
                        // 62进制的UserId
                        .append(RandomNumber.toSixtyTwoRadixString(userId))
                        // 62进制的时间戳
                        .append(RandomNumber.toSixtyTwoRadixString(System.currentTimeMillis()))
                        // 后缀
                        .append(!"".equals(ossResult.getSuffix()) ? "." + ossResult.getSuffix() : "")
                        .toString());
        // 设置文件URL
        ossResult.setUrl(oss.getMainUrl() + ossResult.getKey());

        // 创建临时文件存储路径
        String temFileName = new StringBuilder(System.getProperty("java.io.tmpdir")).append(UUID.randomUUID().toString()).append(".tem").toString();
        File targetFile = new File(temFileName);


        // 开始上传文件
        try {
            // TODO 这个理应不需要将文件暂存

            // 将接收到的文件暂时存在本地
            upFile.transferTo(targetFile);
            // 将暂存文件上传到对象存储中
            oss.localFile(targetFile, ossResult.getKey());

            // 成功
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 删除暂存文件
            targetFile.delete();
        }

        return ossResult;
    }

    /**
     * 上传文件
     * @param upFile
     * @param userId
     * @return
     */
    @Override
    public ApiJson localFile(HttpServletRequest request, MultipartFile upFile, String userId, Integer parentPathId) throws IOException {
        HttpSession session = request.getSession();
        UserSpace userSpace = (UserSpace) session.getAttribute(SessionKeyEnum.LOCAL_USER.getKey());

        ApiJson apiJson = null;

        // 判断用户上传的文件大小是否符合规范
        if (upFile.getSize() > maxFileSize) {
            return new ApiJson(OssCodeEnum.FILE_LARGE);
        }

        // 判断用户空间大小是否可以容纳新文件
        if (userSpace.getUsedSpace() + upFile.getSize() > userSpace.getSumSpace()) {
            return new ApiJson(OssCodeEnum.NOT_FREE_SPACE);
        }

        Files files = new Files();
        // 设置文件大小
        files.setFileSize(upFile.getSize());
        // 设置文件md5
        files.setMd5(DigestUtils.md5Hex(upFile.getBytes()));

        // 通过原文件判断是否已经有相同的文件存在(如果存在则返回该文件的相关信息)
        OssResult ossResult = getFilesFromFormerly(files);

        // ossResult为空,保存文件到对象存储
        if (ossResult == null) {
            // 保存文件到对象存储中
            ossResult = localFile(upFile, userId, oss);
            // 创建成功信息为秒传并返回相关数据
            apiJson = new ApiJson<>(SystemCodeEnum.SUCCESS);

            // 将Files对象插入到数据库
            this.saveFiles(files, ossResult, userId);

        }
        // ossResult不为空,即该文件已经被上传过
        else {
            // 创建成功信息为秒传并返回相关数据(返回秒传信息)
            apiJson = new ApiJson<>(OssCodeEnum.AGAIN);
            // 设置ossResult对象的文件名
            ossResult.setOriginalName(upFile.getOriginalFilename());
        }

        apiJson.setData(ossResult);

        // 将PathStructure对象插入到数据库
        PathStructure pathStructure = this.savePathStructure(ossResult, userId, parentPathId);

        // 将FileLink保存到数据库
        this.saveFileLink(ossResult, pathStructure, files);

        // 更新User数据
        this.countSpace(userSpace, files);

        return apiJson;
    }

    /**
     * 通过原文件判断是否已经有相同的文件存在(如果存在则返回该文件的相关信息)
     * @param files
     * @return
     */
    private OssResult getFilesFromFormerly(Files files){
        // 根据原提供的部分信息查询是否记录是否存在
        PathAndFile pathAndFile = pathAndFileMapper.selectByFilesSelective(files);

        if (pathAndFile != null) {
            OssResult ossResult = new OssResult();
            // 设置文件Key
            ossResult.setKey(pathAndFile.getFileKey());
            // 设置文件后缀名
            ossResult.setSuffix(pathAndFile.getFileType());
            // 设置URL
            ossResult.setUrl(pathAndFile.getFileLink());
            // 设置文件大小
            ossResult.setSize(files.getFileSize());

            return ossResult;
        }
        return null;
    }

    /**
     * 向数据库中添加Files对象
     * @param files 对应数据库表中的文件信息
     * @param ossResult 对象存储保存后返回的数据
     * @param userId 用户ID,表名该文件是谁上传的
     */
    private void saveFiles(Files files, OssResult ossResult, String userId) {

        // 设置key
        files.setFileKey(ossResult.getKey());
        // 设置UserId
        files.setUserId(userId);
        // 设置文件后缀名
        files.setFileType(ossResult.getSuffix());
        // 设置上传时间戳(秒)
        files.setTime((int) (System.currentTimeMillis()/1000));
        // 保存文件
        filesMapper.insert(files);
    }

    /**
     * 向数据库中添加PathStructure对象
     * @param ossResult 对象存储保存后返回的数据
     * @param userId 用户ID,表名该文件是谁上传的
     * @param parentPathId 父目录ID(指定存放位置,为空便是存放在根路径下)
     * @return
     */
    private PathStructure savePathStructure(OssResult ossResult, String userId, Integer parentPathId) {
        // 将PathStructure对象插入到数据库
        PathStructure pathStructure = new PathStructure();
        // 用户Id
        pathStructure.setUserId(userId);
        // 文件或目录名
        pathStructure.setPathName(ossResult.getOriginalName());
        // 设置该文件或目录的位置
        pathStructure.setParentPathId(parentPathId);
        // 修改时间
        pathStructure.setTime((int)(System.currentTimeMillis()/1000));
        // 设置为文件
        pathStructure.setDirectory(false);

        // 插入记录
        pathStructureMapper.insert(pathStructure);

        return pathStructure;
    }

    /**
     * 保存FileLink
     * @param pathStructure
     * @param files
     * @return
     */
    private FileLink saveFileLink(OssResult ossResult, PathStructure pathStructure, Files files){
        FileLink fileLink = new FileLink();

        fileLink.setPathId(pathStructure.getPathId());
        fileLink.setFileKey(ossResult.getKey());

        // 保存
        fileLinkMapper.insert(fileLink);

        return fileLink;
    }

    private void countSpace(UserSpace userSpace, Files files){
        // 设置已用空间大小
        userSpace.setUsedSpace(userSpace.getUsedSpace() + files.getFileSize());

        // 更新数据
        userSpaceMapper.updateByPrimaryKeySelective(userSpace);
    }
}
