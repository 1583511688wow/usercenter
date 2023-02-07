package com.ljh.userdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ljh.userdemo.common.BaseResponse;
import com.ljh.userdemo.common.ErrorCode;
import com.ljh.userdemo.common.ResultUtils;
import com.ljh.userdemo.exception.BusinessException;
import com.ljh.userdemo.model.domain.User;
import com.ljh.userdemo.model.domain.request.UserLoginRequest;
import com.ljh.userdemo.model.domain.request.UserRegisterRequest;
import com.ljh.userdemo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ljh.userdemo.constant.UserConstant.ADMIN_ROLE;
import static com.ljh.userdemo.constant.UserConstant.USER_LOGIN_STATE;
/**
 * 用户接口
 *
 * @author 李俊豪
 */



@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //用户注册
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){

        if (userRegisterRequest == null){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求为空");
        }

        //请求参数校验
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }


        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);

        if (result == 0){
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户注册失败");
        }

        return ResultUtils.success(result);

    }

    //用户登入
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){

        if (userLoginRequest == null){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求体为空");
        }

        //请求参数校验
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");

        }


        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);

    }



    @GetMapping("/currentUser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){

        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) attribute;

        if (currentUser == null){

            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登入");


        }

        Long id = currentUser.getId();
        // todo 校验用户是否合法
        User user = userService.getById(id);


        return ResultUtils.success(user);

    }




    //根据用户名查询用户
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers( String userName, HttpServletRequest request){

        BaseResponse<Boolean> admin = isAdmin(request);

        //鉴权-仅管理员可查询
        if (admin == null){

            throw new BusinessException(ErrorCode.NO_AUTH);
        }


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)){
            queryWrapper.like("username",userName);

        }

        List<User> userList = userService.list(queryWrapper);

        List<User> userList1 = userList.stream()
                .map(user -> userService.getSafeUser(user))
                .collect(Collectors.toList());
        return  ResultUtils.success(userList1);
    }

    //根据id删除用户
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUsers( @RequestBody long id, HttpServletRequest request){


        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数小于0");
        }

        //鉴权-仅管理员可查询
        BaseResponse<Boolean> admin = isAdmin(request);
        if (admin == null){

            throw new BusinessException(ErrorCode.NO_AUTH);


        }

        boolean isRemove = userService.removeById(id);

        if (!isRemove){

            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }



        return ResultUtils.success(true);
    }


    //鉴权-仅管理员可查询
    public BaseResponse<Boolean> isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);

        User user = (User) userObj;

        boolean isSuccess = (user != null && user.getUserRole() == ADMIN_ROLE);

        if (!isSuccess){


            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        return ResultUtils.success(true);
    }


    //用户注销
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){

        if (request ==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求为空");
        }
        Integer logout = userService.userLogout(request);

        return ResultUtils.success(logout);

    }


}
