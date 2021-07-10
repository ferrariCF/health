package com.lxin.health.dao;

import com.lxin.health.pojo.OrderSetting;

import java.util.Date;

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
}
