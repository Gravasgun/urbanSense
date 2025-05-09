package com.cqupt.urbansense.enums;

public enum AppHttpCodeEnum {
    // 成功段0
    SUCCESS(200, "操作成功"),
    // 登录段1~50
    NEED_LOGIN(1, "需要登录后操作"),
    LOGIN_FAILED(2, "登录失败"),
    LOGIN_PASSWORD_ERROR(2, "密码错误"),
    // TOKEN50~100
    TOKEN_INVALID(50, "无效的TOKEN"),
    TOKEN_EXPIRE(51, "TOKEN已过期"),
    TOKEN_REQUIRE(52, "TOKEN是必须的"),
    // SIGN验签 100~120
    SIGN_INVALID(100, "无效的SIGN"),
    SIG_TIMEOUT(101, "SIGN已过期"),
    // 参数错误 500~1000
    PARAM_REQUIRE(500, "缺少参数"),
    PARAM_INVALID(501, "无效参数"),
    PARAM_IMAGE_FORMAT_ERROR(502, "图片格式有误"),
    SERVER_ERROR(503, "服务器内部错误"),
    // 数据错误 1000~2000
    DATA_EXIST(1000, "数据已经存在"),
    WECHAT_USER_DATA_NOT_EXIST(1001, "微信用户数据不存在"),
    PLATFORM_ID_DOT_EXIST(1002, "平台ID不存在"),
    CLASSIFICATION_DOT_EXIST(1003, "问题分类不能为空"),
    DESCRIPTION_DOT_EXIST(1004, "问题描述不能为空"),
    DATA_NOT_EXIST(1002, "数据不存在"),
    // 数据错误 3000~3500
    NO_OPERATOR_AUTH(3000, "无权限操作"),
    NEED_ADMIND(3001, "需要管理员权限"),
    CHANNEL_REFERENCED(3002, "频道已被引用，不可禁用"),
    CHANNEL_USED(3003, "频道已被启用，不可删除"),
    // 自媒体文章错误 3501~3600
    MATERIAL_REFERENCE_FAIL(3501, "素材引用失效");


    int code;
    String errorMessage;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
