package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.constant.RedisMessageConstant;
import com.lxin.health.entity.Result;
import com.lxin.health.pojo.Member;
import com.lxin.health.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-13
 **/
@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result check(@RequestBody Map<String,String> loginInfo, HttpServletResponse response){
        // 校验验证码
        Jedis jedis = jedisPool.getResource();
        String telephone = loginInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone;

        String codeInRedis = jedis.get(key);

        if (StringUtils.isEmpty(codeInRedis)) {
            return new Result(false,"请重新发送验证码！");
        }

        if (!codeInRedis.equals(loginInfo.get("validateCode"))) {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 验证码正确，删除redis中的验证码
        log.debug("验证码校验通过 {}",codeInRedis);
        jedis.del(key);
        jedis.close();

        memberService.login(telephone);

        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");
        cookie.setMaxAge(30*24*60*60);
        response.addCookie(cookie);

        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
