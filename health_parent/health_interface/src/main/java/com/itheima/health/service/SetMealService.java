package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/1 11:44
 */
public interface SetMealService {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void add(Setmeal setmeal, Integer... checkgroupIds);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map> findSetmealCount();

}
