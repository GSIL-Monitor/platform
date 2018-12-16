package cn.laeni.platform.ossfile.service;

import cn.laeni.platform.ossfile.entity.PathAndFile;
import cn.laeni.platform.ossfile.entity.PathStructure;

import java.util.List;

/**
 *
 * @author laeni.cn
 */
public interface PathAndFileService {

    /**
     * 通过"用户ID"查询指定路径下的全部目录
     * @param userId 用户Id
     * @param parentPathId 该文件或文件夹的父路径id(根路径的父路径ID为null)
     * @return
     */
    List<PathStructure> getPathStructureList(String userId, Integer parentPathId);

    /**
     * 获取用户指定目录下的全部文件
     * @param userId 用户Id
     * @param parentPathId 该文件或文件夹的父路径id(根路径的父路径ID为null)
     * @return
     */
    List<PathAndFile> getPathAndFile(String userId, Integer parentPathId);

    /**
     * 获取所有文件和目录(相当于综合前两个Service的功能)
     * @param userId 用户Id
     * @param parentPathId 该文件或文件夹的父路径id(根路径的父路径ID为null)
     * @return
     */
    List<Object> getAllTheFileFolder(String userId, Integer parentPathId);

    /**
     * 获取指定类型的所有文件
     * @param userId
     * @param typeName
     * @return
     */
    List<PathAndFile> getPathAndFileByType(String userId, String typeName);

    /**
     * 查询该用户指定字段的所有文件(不包含文件夹)
     * @param userId 用户Id
     * @param pathName 用户输入字段
     * @return
     */
    List<PathAndFile> selectAllByPathName(String userId, String pathName);

    /**
     * 通过文件夹名、位置插入一条数据
     * @param pathName 文件夹名字
     * @param parentPathId 父目录ID
     * @param userId 用户ID
     * @return
     */
    PathStructure insertPathByPathName(String pathName, Integer parentPathId, String userId);

    /**
     * 重命名文件或目录
     * @param pathId 待重命名的文件或目录ID
     * @param pathName 新名字
     * @return
     */
    PathStructure updateByPathName(Integer pathId, String pathName);

    /**
     * 开通网盘功能
     * @param userId
     * @return
     */
    boolean openService(String userId);
}
