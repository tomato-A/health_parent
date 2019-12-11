package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/1 11:44
 */
@Repository
public interface SetMealDao {
    Page<Setmeal> findPage(String queryString);

    void add(Setmeal setmeal);

    void addSetMealAndCheckGroup(Map<String, Integer> map);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map> findSetmealCount();

}
