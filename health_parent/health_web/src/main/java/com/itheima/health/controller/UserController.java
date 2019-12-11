package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/8 17:41
 */
@RestController
@RequestMapping("/user")
public class UserController {



    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername(){
        User user = null;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
