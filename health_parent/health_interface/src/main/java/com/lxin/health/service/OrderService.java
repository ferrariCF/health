package com.lxin.health.service;

import com.lxin.health.exception.MyException;

import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-13
 **/
public interface OrderService {
    /**
     * 提交预约
     * @param paraMap
     * @return
     */
    Integer submit(Map<String, String> paraMap) throws MyException;

    /**
     * 查询预约订单
     * @param id
     * @return
     */
    Map<String, String> findById(int id);
}
