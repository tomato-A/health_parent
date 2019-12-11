package com.itheima.health.job;

import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/1 17:07
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        Set<String> set = jedisPool.getResource().sdiff(
                RedisMessageConstant.SETMEAL_PIC_RESOURCE,
                RedisMessageConstant.SETMEAL_PIC_DB_DBRESOURCE
        );
        for (String pic : set) {
            //删除图片服务器中的图片文件
            System.out.println("----------");
            QiniuUtils.deleteFileFromQiniu(pic);
            //删除redis中的数据
            jedisPool.getResource().srem(RedisMessageConstant.SETMEAL_PIC_RESOURCE,pic);
        }
    }
}
