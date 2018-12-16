package cn.laeni.platform.ossfile.service.impl;

import cn.laeni.platform.ossfile.entity.PathAndFile;
import cn.laeni.platform.ossfile.entity.PathStructure;
import cn.laeni.platform.ossfile.entity.UserSpace;
import cn.laeni.platform.ossfile.entity.constant.FileTypeEnum;
import cn.laeni.platform.ossfile.mapper.PathAndFileMapper;
import cn.laeni.platform.ossfile.mapper.PathStructureMapper;
import cn.laeni.platform.ossfile.mapper.UserSpaceMapper;
import cn.laeni.platform.ossfile.service.PathAndFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laeni.cn
 */
@Service
public class PathAndFileServiceIml implements PathAndFileService {

    @Autowired
    PathStructureMapper pathStructureMapper;
    @Autowired
    PathAndFileMapper pathAndFileMapper;
    @Autowired
    UserSpaceMapper userSpaceMapper;

    /**
     * 通过"用户ID"查询指定路径下的全部目录
     *
     * @param userId       用户Id
     * @param parentPathId 该文件或文件夹的父路径id(根路径的父路径ID为null)
     * @return
     */
    @Override
    public List<PathStructure> getPathStructureList(String userId, Integer parentPathId) {
        return pathStructureMapper.selectByUserId(userId, parentPathId);
    }

    /**
     * 获取用户指定目录下的全部文件
     *
     * @param userId       用户Id
     * @param parentPathId 该文件或文件夹的父路径id(根路径的父路径ID为null)
     * @return
     */
    @Override
    public List<PathAndFile> getPathAndFile(String userId, Integer parentPathId) {
        return pathAndFileMapper.selectByUserIdAndPathId(userId, parentPathId);
    }

    /**
     * 获取所有文件和目录
     *
     * @param userId       用户Id
     * @param parentPathId 该文件或文件夹的父路径id(根路径的父路径ID为null)
     * @return
     */
    @Override
    public List<Object> getAllTheFileFolder(String userId, Integer parentPathId) {
        // 0代表根目录
        if (Integer.valueOf(0).equals(parentPathId)) {
            parentPathId = null;
        }

        ArrayList<Object> list = new ArrayList<>();
        // 查询所有文件夹
        list.addAll(this.getPathStructureList(userId, parentPathId));
        // 查询所有文件
        list.addAll(this.getPathAndFile(userId, parentPathId));

        return list;
    }

    /**
     * 根据文件类型查询用户下所有指定类型的文件
     * @param userId
     * @param typeName
     * @return
     */
    @Override
    public List<PathAndFile> getPathAndFileByType(String userId, String typeName) {

        // 文档
        if (FileTypeEnum.DOCUMENT.getTypeName().equals(typeName)) {
            return pathAndFileMapper.selectByType(userId, FileTypeEnum.DOCUMENT.getFullName());
        }
        // 图片
        else if (FileTypeEnum.IMAGE.getTypeName().equals(typeName)) {
            return pathAndFileMapper.selectByType(userId, FileTypeEnum.IMAGE.getFullName());
        }
        // 视频
        else if (FileTypeEnum.VIDEO.getTypeName().equals(typeName)) {
            return pathAndFileMapper.selectByType(userId, FileTypeEnum.VIDEO.getFullName());
        }
        // 音乐
        else if (FileTypeEnum.MUSIC.getTypeName().equals(typeName)) {
            return pathAndFileMapper.selectByType(userId, FileTypeEnum.MUSIC.getFullName());
        }

        return null;
    }

    /**
     * 根据关键字利用数据库的LIKE简单搜索文件
     * @param userId 用户Id
     * @param pathName 用户输入字段
     * @return
     */
    @Override
    public List<PathAndFile> selectAllByPathName(String userId, String pathName) {

        //判断字段前后是否有%或者_
        char[] pathNameChars = pathName.toCharArray();

        // 定义一个新数组
        char[] newPathNameChars = new char[pathNameChars.length * 2];
        int i = 0;

        for (char c : pathNameChars) {
            if (c == '%' || c == '_') {
                newPathNameChars[i++] = '\\';
            }
            newPathNameChars[i++] = c;
        }

        pathName = String.copyValueOf(newPathNameChars);

        pathName = "%" + pathName.trim() + "%";
        return pathAndFileMapper.selectAllByPathName(userId, pathName);
    }

    /**
     * 创建文件夹
     * @param pathName 文件夹名字
     * @param parentPathId 父目录ID
     * @param userId 用户ID
     * @return
     */
    @Override
    public PathStructure insertPathByPathName(String pathName, Integer parentPathId, String userId) {

        // 创建一个新文件夹对象
        PathStructure pathStructure = new PathStructure();
        pathStructure.setUserId(userId);
        pathStructure.setParentPathId(parentPathId);
        pathStructure.setPathName(pathName);
        // 设置当期时间(时间戳精确到秒)
        pathStructure.setTime((int)(System.currentTimeMillis()/1000));
        // 设置其为目录
        pathStructure.setDirectory(true);

        if (pathStructureMapper.insert(pathStructure) > 0) {
            return pathStructure;
        } else {
            return null;
        }
    }

    /**
     * 重命名文件或目录
     * @param pathId 待重命名的文件或目录ID
     * @param pathName 新名字
     * @return
     */
    @Override
    public PathStructure updateByPathName(Integer pathId, String pathName) {

        // 创建一个新文件夹(文件)对象
        PathStructure pathStructure = new PathStructure();
        // 设置主键
        pathStructure.setPathId(pathId);
        // 设置需要修改的内容
        pathStructure.setPathName(pathName);
        // 设置当期时间(时间戳精确到秒)
        pathStructure.setTime((int) (System.currentTimeMillis() / 1000));

        if (pathStructureMapper.updateByPrimaryKeySelective(pathStructure) > 0) {
            return pathStructure;
        } else {
            return null;
        }
    }

    /**
     * 开通网盘功能
     * @param userId
     * @return
     */
    @Override
    public boolean openService(String userId) {
        UserSpace userSpace = new UserSpace();
        userSpace.setUserId(userId);
        // 设置用户总空间容量(2G)
        userSpace.setSumSpace(2147483648L);

        if (userSpaceMapper.insertSelective(userSpace) > 0) {
            return true;
        }
        return false;
    }
}
