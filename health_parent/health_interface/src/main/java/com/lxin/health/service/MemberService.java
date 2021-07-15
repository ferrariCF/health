package com.lxin.health.service;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-13
 **/
public interface MemberService {
    /**
     * 手机快速登录
     * @param telephone
     */
    void login(String telephone);

    /**
     * 查询过去12个月，每一个月的总会员数
     * @param months
     * @return
     */
    List<Integer> getMemberReport(List<String> months);
}
