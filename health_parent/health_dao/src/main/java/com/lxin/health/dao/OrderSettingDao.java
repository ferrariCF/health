package com.lxin.health.dao;

import com.lxin.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-10
 **/
public interface OrderSettingDao {
    /**
     * 通过日期查询预约设置信息
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 更新最大预约数
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 添加预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据月份查询预约信息
     * @param month
     * @return
     */
    List<Map<String, Integer>> getDataByMonth(String month);
}
