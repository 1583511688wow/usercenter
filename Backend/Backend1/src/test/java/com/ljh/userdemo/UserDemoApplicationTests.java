package com.ljh.userdemo;


import com.ljh.userdemo.model.domain.User;
import com.ljh.userdemo.service.UserService;
import com.ljh.userdemo.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 单元测试类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserDemoApplicationTests extends UserServiceImpl {

   @Resource
   private UserService userService;

    @Test
    public void demo() {

         String encryptPassword = "158351";
         String userPassword = "158351";



    }



}
