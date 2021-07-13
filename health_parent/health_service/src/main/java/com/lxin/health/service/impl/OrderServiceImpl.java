package com.lxin.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxin.health.dao.MemberDao;
import com.lxin.health.dao.OrderDao;
import com.lxin.health.dao.OrderSettingDao;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.Member;
import com.lxin.health.pojo.Order;
import com.lxin.health.pojo.OrderSetting;
import com.lxin.health.service.OrderService;
import com.lxin.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-13
 **/
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 提交预约 实现
     *
     * @param paraMap
     * @return
     */
    @Override
    @Transactional
    public Integer submit(Map<String, String> paraMap) {
        // 获取预约日期
        String orderDateString = paraMap.get("orderDate");
        Date orderDate;
        try {
            orderDate = DateUtils.parseString2Date(orderDateString);
        } catch (Exception e) {
            throw new MyException("预约日期格式不正确");
        }
        // 查询预约设置，判断是否可以预约
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);

        // 不能预约
        if (orderSetting == null) {
            throw new MyException("所选日期不能预约，请选择其它日期");
        }
        // 预约满
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            throw new MyException("所选日期不能预约，请选择其它日期");
        }

        // 可以预约
        // 设置预约，用于判断是否重复预约和添加预约
        Order order = new Order();
        order.setOrderDate(orderDate);
        order.setSetmealId(Integer.valueOf(paraMap.get("setmealId")));

        // 获取telephone，查询member，判断是否已经注册过
        String telephone = paraMap.get("telephone");
        Member member = memberDao.findByTelephone(telephone);

        if (member == null) {
            // 未注册过
            member = new Member();
            member.setName(paraMap.get("name"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setSex(paraMap.get("sex"));
            String idCard = paraMap.get("idCard");
            member.setIdCard(idCard);
            member.setPassword(idCard.substring(idCard.length()-6));
            member.setRemark("微信预约注册");

            memberDao.add(member);
            order.setMemberId(member.getId());
        }else{
            //已注册过，根据memberId，setmealId和orderDate查询，判断是否重复预约
            Integer memberId = member.getId();
            order.setMemberId(memberId);

            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                throw new MyException("已经预约过了，请勿重复预约");
            }
        }

        // 不是重复预约
        // 更新t_ordersetting的reservations
        Integer effectRows = orderSettingDao.editReservationsByOrderDate(orderSetting);
        if (effectRows == 0) {
            // reservations >= number
            throw new MyException("预约已满，请选择其它日期");
        }

        // 添加预约，返回id
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType(paraMap.get("orderType"));
        orderDao.add(order);

        return order.getId();
    }

    /**
     * 查询预约订单
     * @param id
     * @return
     */
    @Override
    public Map<String, String> findById(int id) {
        return orderDao.findById4Detail(id);
    }
}
