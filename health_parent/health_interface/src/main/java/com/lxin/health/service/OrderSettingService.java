package com.lxin.health.service;

import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.OrderSetting;

import java.util.List;

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
}
