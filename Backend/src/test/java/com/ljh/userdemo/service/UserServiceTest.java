package com.ljh.userdemo.service;
import java.util.Date;

import com.ljh.userdemo.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 李俊豪
 *
 */


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;


    //添加用户demo
    @Test
    public void testAddUser(){

        User user = new User();
        user.setUsername("ljhljh");
        user.setUserAccount("158351");
        user.setAvatarUrl("https://profile.csdnimg.cn/0/A/7/0_qq_53833546");
        user.setGender(0);
        user.setUserPassword("158351");
        user.setPhone("123");
        user.setEmail("456");

        boolean isSuccess = userService.save(user);
        System.out.println(user.getId());
        assertTrue(isSuccess);


    }

    //用户注册测试
    @Test
    void userRegister() {

        //密码非空校验
        String userAccount = "ljhhhh1";
        String userPassword = "";
        String checkPassword = "1236";
        String planetCode = "1236";
        long l = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        Assertions.assertEquals(-1, l);

        //校验账户长度不小于4位
        userAccount = "op";
         l = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, l);

        //校验密码长度不小于8位
        userAccount = "ljhhhhh2";
        userPassword = "123456";
        checkPassword = "123456";
        l = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, l);

        //校验不包含特殊字符
        userAccount = "ljh hhhh";
        userPassword = "12345679797";
        checkPassword = "12345679797";
        l = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, l);

       //校验密码和校验密码相同
        userAccount = "ljhhhhh3";
        userPassword = "12345679797";
        checkPassword = "123456797597";
        l = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, l);

        //校验账户不能重复
        userAccount = "158351";
        userPassword = "12345679797";
        checkPassword = "12345679797";
        l = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, l);


        userAccount = "48484sa446";
        userPassword = "12345679797";
        checkPassword = "12345679797";
        l = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, l);






    }
}