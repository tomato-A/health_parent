package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/10 17:01
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;



    /**
     * 获得运营统计数据
     * Map数据格式：
     *      todayNewMember（今日新增会员数） -> number
     *      totalMember（总会员数） -> number
     *      thisWeekNewMember（本周新增会员数） -> number
     *      thisMonthNewMember（本月新增会员数） -> number
     *      todayOrderNumber（今日预约数） -> number
     *      todayVisitsNumber（今日到诊数） -> number
     *      thisWeekOrderNumber（本周预约数） -> number
     *      thisWeekVisitsNumber（本周到诊数） -> number
     *      thisMonthOrderNumber（本月预约数） -> number
     *      thisMonthVisitsNumber（本月到诊数） -> number
     *      hotSetmeal（热门套餐（取前4）） -> List<Map<String,Object>>
     */
    @Override
    public Map<String, Object> getBusinessReport() throws Exception {
        Map map = new HashMap<>();
        // 获得当前日期
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday());
        // 获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 获取本周最后一天的日期
        String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        // 获得本月第一天的日期
        String thisMonthFirst = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        // 获取本月最后一天的日期
        String thisMonthLast = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

        /***********************会员相关统计***********************************/
        // 今日新增会员数
        Integer todayNewMember = memberDao.findTodayNewMember(reportDate);
        // 总会员数
        Integer totalMember = memberDao.findTotalMember();
        // 本周新增会员数
        Integer thisWeekNewMember = memberDao.findThisNewMember(thisWeekMonday);
        // 本月新增会员数
        Integer thisMonthNewMember = memberDao.findThisNewMember(thisMonthFirst);

        /************************预约订单相关统计*********************************/
        // 今日预约数
        Integer todayOrderNumber = orderDao.findTodayOrderNumber(reportDate);
        // 今日到诊数
        Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(reportDate);

        //周
        Map<String,Object> weekMap = new HashMap<>();
        weekMap.put("begin",thisWeekMonday);
        weekMap.put("end",thisWeekSunday);
        // 本周预约数
        Integer thisWeekOrderNumber = orderDao.findThisOrderNumber(weekMap);
        // 本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findThisVisitsNumber(weekMap);

        //月
        Map<String,Object> monthMap = new HashMap<>();
        monthMap.put("begin",thisMonthFirst);
        monthMap.put("end",thisMonthLast);
        // 本月预约数
        Integer thisMonthOrderNumber = orderDao.findThisOrderNumber(monthMap);
        // 本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findThisVisitsNumber(monthMap);

        /*****************************热门套餐*********************************************/
        // 热门套餐（取前4）
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        map.put("reportDate",reportDate); // String

        map.put("todayNewMember",todayNewMember); // Integer
        map.put("totalMember",totalMember); // Integer
        map.put("thisWeekNewMember",thisWeekNewMember); // Integer
        map.put("thisMonthNewMember",thisMonthNewMember); // Integer

        map.put("todayOrderNumber",todayOrderNumber); // Integer
        map.put("todayVisitsNumber",todayVisitsNumber); // Integer
        map.put("thisWeekOrderNumber",thisWeekOrderNumber); // Integer
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber); // Integer
        map.put("thisMonthOrderNumber",thisMonthOrderNumber); // Integer
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber); // Integer

        map.put("hotSetmeal",hotSetmeal); // List<Map>

        return map;
    }
}
