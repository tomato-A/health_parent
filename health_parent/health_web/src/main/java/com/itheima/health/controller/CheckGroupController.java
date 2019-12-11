package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/11/29 16:41
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = checkGroupService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        if (checkGroup != null) {
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id){
        List<Integer> list = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return list;
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer...checkItemIds){
        try {
            checkGroupService.edit(checkGroup,checkItemIds);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            checkGroupService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false,e.getMessage());
        } catch (Exception e){
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> list = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
