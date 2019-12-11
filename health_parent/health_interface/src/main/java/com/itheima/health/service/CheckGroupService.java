package com.itheima.health.service;


import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/11/29 16:47
 */
public interface CheckGroupService {
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void add(CheckGroup checkGroup, Integer... checkitemIds);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer... checkItemIds);

    void delete(Integer id);

    List<CheckGroup> findAll();
}
