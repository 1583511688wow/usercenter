package com.ljh.userdemo.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登入请求体
 *
 * @author 李俊豪
 */

@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = 2098250897809701140L;

    private String userAccount;

    private String userPassword;



}
