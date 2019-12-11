package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;


/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/1 11:43
 */
@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setMealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }


    // 图片上传，上传到七牛云
    @RequestMapping("/upload")
    public Result upload(@RequestParam(value = "imgFile")MultipartFile imgFile){
        // 如何获取文件名(1.jpg)
        String filename = null;
        try {
            filename = imgFile.getOriginalFilename();
            // 使用UUID的方式生成文件名
            filename= UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
            // 上传图片
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
            // 向七牛云保存图片的同时，再向Redis中存放数据（目的：用于删除七牛云上的垃圾图片），采用Redis中的集合存储
            jedisPool.getResource().sadd(RedisMessageConstant.SETMEAL_PIC_RESOURCE,filename);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer...checkgroupIds){
        try {
            setMealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_SETMEAL_FAIL);

        }
    }
}
