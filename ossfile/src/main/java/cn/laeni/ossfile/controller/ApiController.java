package cn.laeni.ossfile.controller;

import cn.laeni.ossfile.entity.PathAndFile;
import cn.laeni.ossfile.entity.User;
import cn.laeni.ossfile.entity.constant.RequestKeyEnum;
import cn.laeni.ossfile.entity.constant.SystemCodeEnum;
import cn.laeni.ossfile.entity.other.ApiJson;
import cn.laeni.ossfile.service.OssFileService;
import cn.laeni.ossfile.service.PathAndFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * 这里所有的Controller均通过js进行调用, 返回数据格式为Json格式
 *
 * @author laeni.cn
 */
@RestController
public class ApiController {

    @Autowired
    PathAndFileService pathAndFileService;
    @Autowired
    OssFileService ossFileService;

    /**
     * 查询指定路径下的全部文件和目录
     * 即查询id为"userId"的用户中,"pathId"对应的目录下所有文件和目录
     *
     * @param parentPathId 该文件或文件夹的父路径id(根路径的父路径ID为null)
     * @return 包含数据的json对象
     */
    @RequestMapping("/getPathStructureList")
    public ApiJson getPathStructureList(HttpServletRequest request, Integer parentPathId) {

        // 创建返回的JSON对象
        ApiJson<SystemCodeEnum> apiJson = new ApiJson<>(SystemCodeEnum.SUCCESS);

        // 获取UserId
        String userId = ((User) request.getAttribute(RequestKeyEnum.USER.getKey())).getUserId();

        // 获取需要返回的数据
        List<Object> list = pathAndFileService.getAllTheFileFolder(userId, parentPathId);

        // 设置需要返回的数据
        apiJson.setData(list);

        return apiJson;
    }

    /**
     * 根据文件类型查询用户下所有指定类型的文件
     *
     * @param typeName
     * @return
     */
    @RequestMapping("/getPathAndFileByType")
    public ApiJson getPathAndFileByType(HttpServletRequest request, String typeName) {

        // 创建返回的JSON对象
        ApiJson<SystemCodeEnum> apiJson = new ApiJson<>(SystemCodeEnum.SUCCESS);

        // 获取UserId
        String userId = ((User) request.getAttribute(RequestKeyEnum.USER.getKey())).getUserId();

        // 查询数据
        apiJson.setData(pathAndFileService.getPathAndFileByType(userId, typeName));

        return apiJson;
    }

    /**
     * 根据关键字利用数据库的LIKE简单搜索文件
     *
     * @param request
     * @param pathName 搜索关键字
     * @return 返回包含 List<PathAndFile> 的apiJson
     */
    @RequestMapping("/selectAllByPathName")
    public ApiJson selectAllByPathName(HttpServletRequest request, String pathName) throws UnsupportedEncodingException {

        // 创建返回的JSON对象
        ApiJson<SystemCodeEnum> apiJson = new ApiJson<>(SystemCodeEnum.SUCCESS);

        // 获取UserId
        String userId = ((User) request.getAttribute(RequestKeyEnum.USER.getKey())).getUserId();

        // 获取需要返回的数据(传过来的字符需要进行URL解码)
        List<PathAndFile> list = pathAndFileService.selectAllByPathName(userId, URLDecoder.decode(pathName, "utf-8"));

        apiJson.setData(list);

        return apiJson;
    }

    /**
     * 创建文件夹
     * TODO 判断该名字是否已经有存在
     * @param request
     * @param pathName 文件夹的名字
     * @param parentPathId 指明在哪个文件夹下创建
     * @return
     */
    @RequestMapping("/insertPathByPathName")
    public ApiJson insertPathByPathName(HttpServletRequest request, String pathName, Integer parentPathId) {

        // 获取UserId
        String userId = ((User) request.getAttribute(RequestKeyEnum.USER.getKey())).getUserId();

        ApiJson apiJson = new ApiJson(SystemCodeEnum.SUCCESS);

        apiJson.setData(pathAndFileService.insertPathByPathName(pathName, parentPathId, userId));

        return apiJson;
    }

    /**
     * 重命名指定文件或目录名
     * @param request
     * @param pathId 文件或目录ID
     * @param pathName 新文件或目录名
     * @return
     */
    @RequestMapping("/reName")
    public ApiJson reName(HttpServletRequest request,Integer pathId,String pathName) {
        ApiJson apiJson = new ApiJson(SystemCodeEnum.SUCCESS);
        apiJson.setData(pathAndFileService.updateByPathName(pathId,pathName));
        return apiJson;
    }

    /**
     * 上传文件,上传成功后返回该文件的相关信息(其中包括网络URL)
     *
     * @param upFile
     * @return
     */
    @RequestMapping(value = "/localFile", produces = "application/json; charset=utf-8")
    public ApiJson localFile(HttpServletRequest request, @RequestParam("upFile") MultipartFile upFile, Integer parentPathId) throws IOException {

        // 获取UserId
        String userId = ((User) request.getAttribute(RequestKeyEnum.USER.getKey())).getUserId();

        return ossFileService.localFile(request, upFile, userId, parentPathId);
    }

    /**
     * 开通网盘功能
     * @param request
     * @return
     */
    @RequestMapping("/openService")
    public ApiJson openService(HttpServletRequest request) {

        // 获取UserId
        String userId = ((User) request.getAttribute(RequestKeyEnum.USER.getKey())).getUserId();

        try {
            pathAndFileService.openService(userId);
        } catch (Exception e) {
            return new ApiJson(SystemCodeEnum.OTHER);
        }

        return new ApiJson(SystemCodeEnum.SUCCESS);
    }

    /**
     * 删除文件
     *
     * @param pathId 在后台唯一和一个用户的文件或目录对应的值,用户不会直接接触该值
     * @param force  是否为强制注册(不为空表示强制删除)
     */
    public void delFile(String pathId, String force) {
        // TODO
        // 删除文件时,先判断是否进行强制删除
        // 如果是则不去判断该文件是否被其他模块引用
        // 如果不是则先判断该文件是否有其他模块引用,如果有则返回被引用的提示信息

        // 这里的删除分为两步,第一步是删除用户的文件,
        // 第二部是删除真正的文件,如果一个文件被多个用户共享,则其中一个用户删除自己的文件不会影响其他用户的使用
    }

    /**
     * 提供一个接口供其他模块声明某个文件在什么地方被引用了多少次
     * 即表示地址为fileUrl的文件被ObjType引用了s次
     *
     * @param fileUrl 文件的网络地址
     * @param ObjType 该文件的使用者
     * @param s       被引用的次数
     */
    public void setFileLink(String fileUrl, String ObjType, Integer s) {
        // TODO
        // 首先对fileUrl进行拆分,把key和主机名提取出来(https://xx.xxx.com/xx1/xx2.jpg 中,key为"xx1/xx2.jpg",主机名为"xx.xxx.com")
        // 通过主机名判断和key判断该文件是否在系统中真实存在,如果存在则继续下面的操作
    }

    // 移动指定文件或目录到其他文件或目录中(逻辑复杂,暂不实现)
    // 复制(逻辑复杂,暂不实现)

}
