package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/8 15:16
 */
@Repository
public interface PermissionDao {

    //使用角色id查询权限集合
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
