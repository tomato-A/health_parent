package com.itheima.health.dao;

import com.itheima.health.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/8 14:59
 */
@Repository
public interface UserDao {
    //查询用户
    User findUserByUsername(String username);
}
