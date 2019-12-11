package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/11/29 16:49
 */
@Repository
public interface CheckGroupDao {
    Page<CheckGroup> selectByCondition(String queryString);

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteAssociation(Integer id);

    void edit(CheckGroup checkGroup);

    long findCountByCheckGroup(Integer id);

    void delete(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupListById(Integer id);
}
