package com.itheima.health.service;

import com.itheima.health.pojo.User;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/8 14:56
 */
public interface UserService {
    User findUserByUsername(String username);
}
