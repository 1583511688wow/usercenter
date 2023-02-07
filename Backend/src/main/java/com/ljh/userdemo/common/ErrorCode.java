package com.ljh.userdemo.common;

/**
 *
 * 错误码
 *
 * @author 李俊豪
 */
public enum ErrorCode {

    SUCCESS(0, "OK", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为错误", ""),
    NO_AUTH(40101, "无权限", ""),
    NOT_LOGIN(40100, "未登入", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    SQLOPERATION_ERROR(40010, "SQL语句操作异常", "");


    private final int code;

    private final String message;

    private final String description;


    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
