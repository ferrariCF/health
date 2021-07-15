package com.lxin.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxin.health.dao.MemberDao;
import com.lxin.health.dao.OrderDao;
import com.lxin.health.service.ReportService;
import com.lxin.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-15
 **/
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 查询运营统计数据
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //周一
        String monday = sdf.format(DateUtils.getThisWeekMonday());
        //1号
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDay4ThisMonth());
        //周日
        String sunday = sdf.format(DateUtils.getSundayOfThisWeek());
        //本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        //reportDate
        String reportDate = sdf.format(today);
        //todayNewMember
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //totalMember
        Integer totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);
        //todayOrderNumber
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //todayVisitsNumber
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        //thisWeekOrderNumber
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday,sunday);
        //thisWeekVisitsNumber
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        //hotSetmeal
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        Map<String,Object> resultMap = new HashMap<>(12);
        resultMap.put("reportDate",reportDate);
        resultMap.put("todayNewMember",todayNewMember);
        resultMap.put("totalMember",totalMember);
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);
        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        resultMap.put("hotSetmeal",hotSetmeal);

        return resultMap;
    }
}
