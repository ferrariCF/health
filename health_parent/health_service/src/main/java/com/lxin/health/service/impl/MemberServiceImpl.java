package com.lxin.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxin.health.dao.MemberDao;
import com.lxin.health.pojo.Member;
import com.lxin.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author: lee
 * @date: 2021-07-13
 **/
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 手机快速登录
     * @param telephone
     */
    @Override
    public void login(String telephone) {
        Member member = memberDao.findByTelephone(telephone);
        if (member == null) {
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRemark("手机快速登录");
            member.setRegTime(new Date());
            memberDao.add(member);
        }
    }
}
