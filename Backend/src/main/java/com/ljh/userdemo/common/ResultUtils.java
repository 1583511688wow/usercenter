package com.ljh.userdemo.common;

/**
 * 返回工具类
 *
 * @author 李俊豪
 */


public class ResultUtils {


    /**
     *
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T>BaseResponse<T> success(T data){


        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */

    public static BaseResponse error(ErrorCode errorCode){

        return new BaseResponse(errorCode);

    }

    public static BaseResponse error(ErrorCode errorCode, String message, String description){

        return new BaseResponse(errorCode.getCode(), message,description);

    }

    public static BaseResponse error(ErrorCode errorCode , String description){

        return new BaseResponse(errorCode.getCode(), errorCode.getMessage(), description);

    }

    public static BaseResponse error(int code , String message, String description){

        return new BaseResponse(code, null,message, description);

    }

}
