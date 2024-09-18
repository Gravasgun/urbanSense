package com.cqupt.urbansense.dtos;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class IssueDto implements Serializable {
    /**
     * 微信用户UnionId
     */
    private String unionId;
    /**
     * 平台id
     */
    private Integer platformId;
    /**
     * 问题分类
     */
    private String classification;
    /**
     * 问题描述
     */
    private String description;
    /**
     * 具体位置描述（可选）
     */
    private String locationDescription;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 照片URL列表，JSON格式存储（如JSON数组）
     */
    private List<String> photoFileUrls;
    /**
     * 视频URL列表，JSON格式存储（如JSON数组）
     */
    private List<String> videoFileUrls;
}
