package com.cqupt.urbansense.service;

import java.io.InputStream;

public interface FileStorageService {
    /**
     * 上传图片文件或视频文件
     *
     * @param filename    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    String uploadFile(String filename, InputStream inputStream, boolean isPhoto);

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
}
