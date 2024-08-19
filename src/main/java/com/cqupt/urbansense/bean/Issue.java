package com.cqupt.urbansense.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("issue")
public class Issue implements Serializable {
    /**
     * 问题ID，主键，自增
     */
    @TableId(value = "issue_id", type = IdType.AUTO)
    private Integer issueId;
    /**
     * 微信用户union_id
     */
    @TableField("union_id")
    private String unionId;
    /**
     * 平台id
     */
    @TableField("platform_id")
    private int platformId;
    /**
     * 问题分类
     */
    @TableField("classification")
    private String classification;
    /**
     * 问题描述
     */
    @TableField("description")
    private String description;
    /**
     * 问题状态（如1-未处理、2-处理中、3-已处理且已解决、4-已处理但未解决）
     */
    @TableField("status")
    private int status;
    /**
     * 具体位置描述（可选）
     */
    @TableField("location_description")
    private String locationDescription;
    /**
     * 经度
     */
    @TableField("longitude")
    private Double longitude;
    /**
     * 纬度
     */
    @TableField("latitude")
    private Double latitude;
    /**
     * 照片URL列表，JSON格式存储（如JSON数组）
     */
    @TableField("photo_url")
    private List<String> photoUrl;
    /**
     * 视频URL列表，JSON格式存储（如JSON数组）
     */
    @TableField("video_urls")
    private List<String> videoUrl;
    /**
     * 提交时间
     */
    @TableField("submitted_time")
    private Date submittedTime;
    /**
     * 最后更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;
}
