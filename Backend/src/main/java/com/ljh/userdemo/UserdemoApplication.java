package com.ljh.userdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ljh.userdemo.mapper")
public class UserdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserdemoApplication.class, args);
    }


    String dsd = "dsd";

}
