package com.lxin.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxin.health.dao.OrderSettingDao;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.OrderSetting;
import com.lxin.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-10
 **/
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入预约设置
     *
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) {
        //* 遍历List<OrderSetting>
        if (null != orderSettingList) {
            for (OrderSetting orderSetting : orderSettingList) {
                //* 通过日期查询预约设置表
                OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
                //* 如果存在预约设置
                if (osInDB != null) {
                    //  * 判断更新后的最大数是否大等于已预约人数
                    if (orderSetting.getNumber() < osInDB.getReservations()) {
                        //  * 小于，报错 已预约数超过最大预约数，接口异常声明
                        throw new MyException("更新后的最大预约数，不能小于已预约人数");
                    }
                    //  * 大于，则可以更新最大预约数
                    orderSettingDao.updateNumber(orderSetting);
                } else {
                    //* 不存在，则添加预约设置
                    orderSettingDao.add(orderSetting);
                }
            }
        }
        //* 事务控制
    }

    @Override
    public List<Map<String, Integer>> getDataByMonth(String month) {
        month += "%";
        return orderSettingDao.getDataByMonth(month);
    }
}
