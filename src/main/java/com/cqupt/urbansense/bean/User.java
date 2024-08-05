package com.cqupt.urbansense.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    /**
     * 主键，自增
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 微信用户的union_id
     */
    @TableField("union_id")
    private String unionId;
    /**
     * 平台ID
     */
    @TableField("platform_id")
    private int platformId;
    /**
     * 用户名称
     */
    @TableField("username")
    private String username;
    /**
     * 用户电话号码
     */
    @TableField("phone_number")
    private String phoneNumber;
    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;
}
