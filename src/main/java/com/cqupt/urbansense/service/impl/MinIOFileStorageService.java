package com.cqupt.urbansense.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cqupt.urbansense.bean.Issue;
import com.cqupt.urbansense.config.MinIOConfig;
import com.cqupt.urbansense.config.MinIOConfigProperties;
import com.cqupt.urbansense.enums.AppHttpCodeEnum;
import com.cqupt.urbansense.mapper.IssueMapper;
import com.cqupt.urbansense.service.FileStorageService;
import com.cqupt.urbansense.utils.ResponseResult;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@EnableConfigurationProperties(MinIOConfigProperties.class)
@Import(MinIOConfig.class)
@Service
public class MinIOFileStorageService implements FileStorageService {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    @Autowired
    private IssueMapper issueMapper;
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
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadFile(MultipartFile multipartFile) {
        // 1. 参数校验
        if (multipartFile == null || multipartFile.getSize() == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 2. 上传图片到 MinIO 中
        // 文件名生成+拼接
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        Boolean isPhoto = false;
        if (postfix.equals(".jpg") || postfix.equals(".jpeg") || postfix.equals(".png")) {
            isPhoto = true;
        }
        String filePath = builderFilePath(fileName + postfix, isPhoto);
        try {
            String contentType = isPhoto ? "image/jpg" : "video/mp4";  // 根据文件类型设置内容类型
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType(contentType)
                    .bucket(minIOConfigProperties.getBucket())
                    .stream(multipartFile.getInputStream(), multipartFile.getInputStream().available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);

            // 构建返回的 URL
            String urlPath = new StringBuilder(minIOConfigProperties.getReadPath())
                    .append(separator)
                    .append(minIOConfigProperties.getBucket())
                    .append(separator)
                    .append(filePath)
                    .toString();

            // 使用 Map 来构建 JSON 数据
            Map<String, String> responseData = new HashMap<>();
            responseData.put("url", urlPath);
            return ResponseResult.okResult(responseData);
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

    /**
     * 清除不存在的文件
     *
     * @param pathUrl
     * @return
     */
//    @PostConstruct
//    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public ResponseResult removeAbsenceFiles(String pathUrl) {
        try {
            // List objects in the bucket
            Iterable<Result<Item>> myObjects = minioClient.listObjects("urbansense");
            // search database file names
            List<Issue> issueList = issueMapper.selectList(null);
            List<String> photoUrlList = issueList.stream().map(Issue::getPhotoUrls).collect(Collectors.toList());
            List<String> videoUrlList = issueList.stream().map(Issue::getVideoUrls).collect(Collectors.toList());
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                String objectName = item.objectName();
                if (!photoUrlList.isEmpty() && !photoUrlList.contains(objectName)) {
                    this.delete(objectName);
                }
                if (!videoUrlList.isEmpty() && !videoUrlList.contains(objectName)) {
                    this.delete(objectName);
                }
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Error occurred: " + e);
        }
        return ResponseResult.okResult("success！");
    }
}
