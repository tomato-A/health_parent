package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.constant.RedisMobileMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/5 9:36
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //发送短信
        try {
//            SMSUtils.sendShortMessage(telephone,code.toString());
            System.out.println("发送的验证码： " + code);
        } catch (Exception e) {//ClientException e
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码缓存到redis
        jedisPool.getResource().setex(telephone+ RedisMobileMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
        // 发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //手机快速登录时发送手机验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //生成4位数字验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //发送短信
        try {
//            SMSUtils.sendShortMessage(telephone,code.toString());
            System.out.println("发送的验证码是： " + code.toString());
        } catch (Exception e) { //ClientException e
            e.printStackTrace();
            //验证码发送失败
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone+ RedisMobileMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
        //验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
