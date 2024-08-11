package com.cqupt.urbansense.service.impl;

import com.cqupt.urbansense.config.MinIOConfig;
import com.cqupt.urbansense.config.MinIOConfigProperties;
import com.cqupt.urbansense.service.FileStorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@EnableConfigurationProperties(MinIOConfigProperties.class)
@Import(MinIOConfig.class)
@Service
public class MinIOFileStorageService implements FileStorageService {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinIOConfigProperties minIOConfigProperties;
    private final static String separator = "/";
    private final static String PHOTO_DIR = "photo";
    private final static String VIDEO_DIR = "video";

    /**
     * 生成存放路径
     *
     * @param filename 文件名
     * @param isPhoto  是否为图片文件
     * @return 生成的完整存放路径
     */
    public String builderFilePath(String filename, boolean isPhoto) {
        StringBuilder stringBuilder = new StringBuilder();
        // 根据文件类型选择目录前缀
        String typeDir = isPhoto ? PHOTO_DIR : VIDEO_DIR;
        stringBuilder.append(typeDir).append(separator);
        // 添加日期路径
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String todayStr = sdf.format(new Date());
        stringBuilder.append(todayStr).append(separator);
        // 添加文件名
        stringBuilder.append(filename);
        return stringBuilder.toString();
    }

    /**
     * 上传文件
     *
     * @param filename    文件名
     * @param inputStream 文件流
     * @param isPhoto     是否为图片文件
     * @return 文件全路径
     */
    @Override
    public String uploadFile(String filename, InputStream inputStream, boolean isPhoto) {
        String filePath = builderFilePath(filename, isPhoto);
        try {
            String contentType = isPhoto ? "image/jpg" : "video/mp4";  // 根据文件类型设置内容类型
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType(contentType)
                    .bucket(minIOConfigProperties.getBucket())
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getReadPath());
            urlPath.append(separator).append(minIOConfigProperties.getBucket());
            urlPath.append(separator);
            urlPath.append(filePath);
            return urlPath.toString();
        } catch (Exception ex) {
            log.error("minio put file error.", ex);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 删除文件
     *
     * @param pathUrl 文件全路径
     */
    @Override
    public void delete(String pathUrl) {
        String key = pathUrl.replace(minIOConfigProperties.getEndpoint() + "/", "");
        int index = key.indexOf(separator);
        String bucket = key.substring(0, index);
        String filePath = key.substring(index + 1);
        // 删除Objects
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket(bucket).object(filePath).build();
        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            log.error("minio remove file error.  pathUrl:{}", pathUrl);
            e.printStackTrace();
        }
    }


    /**
     * 下载文件
     *
     * @param pathUrl 文件全路径
     * @return 文件流
     */
    @Override
    public byte[] downLoadFile(String pathUrl) {
        String key = pathUrl.replace(minIOConfigProperties.getEndpoint() + "/", "");
        int index = key.indexOf(separator);
        String bucket = key.substring(0, index);
        String filePath = key.substring(index + 1);
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minIOConfigProperties.getBucket()).object(filePath).build());
        } catch (Exception e) {
            log.error("minio down file error.  pathUrl:{}", pathUrl);
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while (true) {
            try {
                if (!((rc = inputStream.read(buff, 0, 100)) > 0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
