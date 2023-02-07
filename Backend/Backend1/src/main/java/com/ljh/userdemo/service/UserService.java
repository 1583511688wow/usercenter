package com.ljh.userdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljh.userdemo.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 李俊豪
 * @description 针对表【user】的数据库操作Service
 * @createDate 2023-01-25 14:47:06
 */
public interface UserService extends IService<User> {



    /**

     * 用户注释
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 检验码
     */

    //用户注册
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);


    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return
     */
    //用户登入
    User userLogin(String userAccount, String userPassword, HttpServletRequest Request);

    /**
     * 用户脱敏感
     *
     * @param user
     * @return
     */
    User getSafeUser(User user);


    /**
     * 用户注销
     *
     * @return
     */
     Integer userLogout(HttpServletRequest request);






}
