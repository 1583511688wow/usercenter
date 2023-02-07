package com.ljh.userdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljh.userdemo.common.ErrorCode;
import com.ljh.userdemo.exception.BusinessException;
import com.ljh.userdemo.model.domain.User;
import com.ljh.userdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.ljh.userdemo.mapper.UserMapper;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ljh.userdemo.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 李俊豪
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-01-25 14:47:06
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     *  盐值: 混淆密码
     */

    private final String SALT = "ljh";



    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1.校验

        if (StringUtils.isAnyBlank(userAccount, userPassword,checkPassword,planetCode)){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");

        }

        if (userAccount.length() < 4){


            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户过短");
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过长");
        }


        //账户不能包含特殊字符
        String Regular = "^[a-zA-Z0-9]{4,20}$";
        Matcher matcher = Pattern.compile(Regular).matcher(userAccount);
        if (!matcher.find()){


            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户有特殊字符");
        }

        if (!userPassword.equals(checkPassword)){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");

        }

        //星球编号检验
        if (planetCode.length() > 5){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");

        }

        //星球编号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        int count = this.count(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号重复");

        }

        //账户不能重复
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        int count1 = this.count(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户重复");

        }

        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //保存用户到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUsername(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);

        boolean save = save(user);
        if (!save){

            throw new BusinessException(ErrorCode.SQLOPERATION_ERROR, "注册保存用户操作失败");
        }


        return user.getId();



    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

     if (StringUtils.isAnyBlank(userAccount, userPassword)){


         throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");


     }

        if (userAccount.length() < 4){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数过短");

        }

        //账户不能包含特殊字符
        String Regular = "^[a-zA-Z0-9]{4,20}$";
        Matcher matcher = Pattern.compile(Regular).matcher(userAccount);
        if (!matcher.find()){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户有特殊字符");
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        String SWD = "DFS";

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            log.info("登入失败！");

            throw new BusinessException(ErrorCode.NULL_ERROR, "用户账号或者密码错误");
        }


        User safeUser = getSafeUser(user);
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, safeUser );


        return safeUser;
    }


    @Override
    public User getSafeUser(User user){

        if (user == null){

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }


        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setUserRole(user.getUserRole());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setPlanetCode(user.getPlanetCode());

        return safeUser;
    }

    @Override
    public Integer userLogout(HttpServletRequest request) {

        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


}




