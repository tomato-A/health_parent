package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.SetMealMobileDao;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealMobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/4 15:23
 */
@Service(interfaceClass = SetMealMobileService.class)
@Transactional
public class SetMealMobileServiceImpl implements SetMealMobileService {

    @Autowired
    private SetMealMobileDao setMealMobileDao;

}
