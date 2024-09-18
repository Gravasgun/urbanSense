package com.cqupt.urbansense.service;

import com.cqupt.urbansense.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStorageService {
    /**
     * 上传图片文件或视频文件
     * @param multipartFile
     * @return
     */
    ResponseResult uploadFile(MultipartFile multipartFile);

    /**
     * 删除文件
     *
     * @param pathUrl 文件全路径
     */
    void delete(String pathUrl);

    /**
     * 下载文件
     *
     * @param pathUrl 文件全路径
     * @return
     */
    byte[] downLoadFile(String pathUrl);

    ResponseResult removeAbsenceFiles(String pathUrl);
}
