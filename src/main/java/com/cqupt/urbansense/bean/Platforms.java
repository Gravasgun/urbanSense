package com.cqupt.urbansense.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("platforms")
public class Platforms {
    /**
     * 平台ID，主键，自增
     */
    @TableId(value = "platform_id",type = IdType.AUTO)
    public Integer platformId;
    /**
     * 平台名称（如微信、微博等）
     */
    @TableField("platform_name")
    public String platformName;
}
