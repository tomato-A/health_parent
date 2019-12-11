package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetMealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Hugh
 * @version 1.0
 * @date 2019/12/8 20:14
 */
@RestController
@RequestMapping("/report")
public class ReportController {


    @Reference
    private MemberService memberService;

    @Reference
    private ReportService reportService;

    @Reference
    private SetMealService setMealService;

    // 统计会员注册的折线图
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            // 1：数据集合，存放months
            List<String> list = new ArrayList<>();

            //获取当前日期之前12个单位的日期，以（calender.MONTH）月为单位
            Calendar calendar = Calendar.getInstance();
            calendar.add(calendar.MONTH,-12);
            for (int i = 0; i < 12; i++) {
                calendar.add(calendar.MONTH,1);
                Date time = calendar.getTime();// 获取当前时间（2019-01，2019-02， 2019-03）
                String date = new SimpleDateFormat("yyyy-MM").format(time);
                list.add(date);
            }

            // 2：数据集合，存放memberCount，根据注册时间完成查询
            List<Integer> memberCount = memberService.findMemberCountByRegTime(list);

            // 构造Map集合
            Map<String, Object> map = new HashMap<>();
            map.put("months",list);     //List<String>
            map.put("memberCount",memberCount);     //List<Integer>

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);

        }

    }
    /**
     * 套餐占比统计
     * @return null;
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            // 存放套餐的名称
            List<String> setmealNames = new ArrayList<>();
            // 存放套餐的名称、对应套餐名称的值
            List<Map> setmealCount = setMealService.findSetmealCount();
            // 遍历套餐的名称，存放到setmealNames中
            if (setmealCount != null && setmealCount.size() > 0) {
                for (Map map : setmealCount) {
                    setmealNames.add((String) map.get("name"));
                }
            }
            // 构造Map集合
            Map<String,Object> map = new HashMap<>();
            map.put("setmealNames",setmealNames);
            map.put("setmealCount",setmealCount);

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map = reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //远程调用报表服务获取报表数据
            Map<String,Object> map = reportService.getBusinessReport();
            // 第一步：取出返回结果数据，准备将报表数据写入到Excel文件中

            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = (Integer)map.get("todayNewMember");
            Integer totalMember = (Integer)map.get("totalMember");
            Integer thisWeekNewMember = (Integer)map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer)map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer)map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer)map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer)map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer)map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer)map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer)map.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");
            // 第二步：获得Excel模板文件绝对路径
            String path = request.getSession().getServletContext().getRealPath("template/report_template.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate); //日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);  // 今天新增会员数
            row.getCell(7).setCellValue(totalMember);  // 总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //热门套餐
            int rowNum = 12;
            for (Map map1 : hotSetmeal) {
                String name = (String) map1.get("name");//套餐名称
                Long setmeal_count = (Long) map1.get("setmeal_count");//预约数量
                BigDecimal proportion = (BigDecimal) map1.get("proportion");//占比
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.doubleValue());
            }
            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
        return null;
    }
}
