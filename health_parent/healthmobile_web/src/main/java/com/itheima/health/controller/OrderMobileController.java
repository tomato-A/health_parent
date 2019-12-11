package com.itheima.health.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMobileMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/5 14:14
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");

        String codeInRedis = jedisPool.getResource().get(telephone + RedisMobileMessageConstant.SENDTYPE_ORDER);
        //校验手机验证码
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //调用体检预约服务
        Result result = null;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();

        }
        //预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息。
        if (result.isFlag()) {
            String orderDate = (String) map.get("orderDate");
            try {
//                SMSUtils.sendShortMessage(telephone,orderDate);
                System.out.println(telephone+"   :   "+orderDate);
            } catch (Exception e) {//ClientException e
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        Map map = null;
        try {
            map = orderService.findById4Detail(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
