package com.cqupt.urbansense.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("classification")
public class Classification {
    /**
     * 分类ID，主键，自增
     */
    @TableId(value = "classification_id", type = IdType.AUTO)
    private Integer classificationId;
    /**
     * 分类名称
     */
    @TableField("classification_name")
    private String classificationName;
    /**
     * 分类描述（可选）
     */
    @TableField("description")
    private String description;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 最后更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;
}
