package com.itheima.health.dao;

import com.itheima.health.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/8 15:13
 */
@Repository
public interface RoleDao {
    //使用用户id查询角色集合
    Set<Role> findRolesByUserId(Integer userId);
}
