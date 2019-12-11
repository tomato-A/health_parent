package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/5 15:09
 */
@Repository
public interface OrderDao {

    List<Order> findOrderListByCondition(Order order);

    void add(Order order);

    Map findById4Detail(Integer id);

    Integer findTodayOrderNumber(String reportDate);

    Integer findTodayVisitsNumber(String reportDate);

    Integer findThisOrderNumber(Map<String, Object> weekMap);

    Integer findThisVisitsNumber(Map<String, Object> weekMap);

    List<Map> findHotSetmeal();

}
