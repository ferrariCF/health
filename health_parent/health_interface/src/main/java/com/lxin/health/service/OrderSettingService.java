package com.lxin.health.service;

import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-10
 **/
public interface OrderSettingService {
    /**
     * 批量导入预约设置
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList) throws MyException;

    /**
     * 根据月份查询预约信息
     * @param month
     * @return
     */
    List<Map<String, Integer>> getDataByMonth(String month);

    /**
     * 基于日历设置可预约人数
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting) throws MyException;
}
