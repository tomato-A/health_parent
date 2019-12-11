package com.itheima.health.service;

import com.itheima.health.entity.Result;

import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/5 15:13
 */
public interface OrderService {

    Result order(Map map) throws Exception;

    Map findById4Detail(Integer id);
}
