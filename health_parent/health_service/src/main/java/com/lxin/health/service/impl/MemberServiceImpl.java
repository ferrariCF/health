package com.lxin.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxin.health.dao.MemberDao;
import com.lxin.health.pojo.Member;
import com.lxin.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    /**
     * 查询过去12个月，每一个月的总会员数
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        List<Integer> memberCount = new ArrayList<>(months.size());

        months.forEach(month ->{
            //拼接最后一天
            month += "-31";
            //查询每个月最后一天前的会员总数
            Integer count = memberDao.findMemberCountBeforeDate(month);
            memberCount.add(count);
        });
        return memberCount;
    }
}
