package com.ljh.userdemo.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 李俊豪
 */

@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = 2098250897809701140L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;


}
