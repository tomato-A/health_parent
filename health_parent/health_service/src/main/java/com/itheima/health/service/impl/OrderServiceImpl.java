package com.itheima.health.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/5 15:13
 * 体检预约服务
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /** 体检预约  */
    @Override
    public Result order(Map map) throws Exception {
        //检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            return new Result(false,MessageConstant.ORDER_FULL);//预约已满，不能预约
        }

        //检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        //如果是会员，防止重复预约（一个会员、一个时间、一个套餐不能重复，否则是重复预约）
        if (member != null) {
            Order order = new Order(member.getId(),date,null,null,Integer.parseInt((String) map.get("setmealId")));
            List<Order> list =  orderDao.findOrderListByCondition(order);
            if(list != null && list.size() > 0){
                //已经完成了预约，不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        //可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //当前用户不是会员，需要添加到会员表
        if (member == null) {
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //保存预约信息到预约表
        Order order = new Order(
                member.getId(),
                date,
                (String)map.get("orderType"),
                (String)map.get("orderStatus"),
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById4Detail(Integer id)  {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            Date date = (Date) map.get("orderDate");
            String orderDate = null;
            try {
                orderDate = DateUtils.parseDate2String(date);
                map.put("orderDate", orderDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
