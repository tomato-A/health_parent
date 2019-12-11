package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/11/28 14:59
 */
@Repository
public interface CheckItemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> findByCondition(String queryString);

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    CheckItem findCheckItemById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemListById(Integer id);
}
