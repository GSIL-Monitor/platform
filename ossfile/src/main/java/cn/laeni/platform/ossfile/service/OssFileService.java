package cn.laeni.platform.ossfile.service;

import cn.laeni.platform.ossfile.entity.other.ApiJson;
import cn.laeni.platform.ossfile.entity.other.Oss;
import cn.laeni.platform.ossfile.entity.other.OssResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 上传文件
 *
 * @author laeni.cn
 */
public interface OssFileService {

    /**
     * 上传文件
     *
     * @param upFile
     * @param oss
     * @return
     */
    OssResult localFile(MultipartFile upFile, String userId, Oss oss);

    ApiJson localFile(HttpServletRequest request, MultipartFile upFile, String userId, Integer parentPathId) throws IOException;
}
