package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.dao.SetMealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/1 11:46
 */
@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;


    @Autowired
    private JedisPool jedisPool;

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setMealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Setmeal setmeal, Integer... checkgroupIds) {
        setMealDao.add(setmeal);
        if(checkgroupIds != null && checkgroupIds.length > 0) {
            addSetMealAndCheckGroup(setmeal.getId(), checkgroupIds);
        }
        jedisPool.getResource().sadd(RedisMessageConstant.SETMEAL_PIC_DB_DBRESOURCE,setmeal.getImg());
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    //查询套餐的名称，和套餐预约次数的值
    @Override
    public List<Map> findSetmealCount() {

        return setMealDao.findSetmealCount();
    }

    public void addSetMealAndCheckGroup(Integer setMealId, Integer[] checkGroupIds){
        if(checkGroupIds != null && checkGroupIds.length > 0){
            for (Integer checkGroupId : checkGroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setMealId",setMealId);
                map.put("checkGroupId",checkGroupId);
                setMealDao.addSetMealAndCheckGroup(map);
            }
        }
    }

}
