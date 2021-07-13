package com.lxin.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.StringUtil;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.constant.RedisMessageConstant;
import com.lxin.health.entity.Result;
import com.lxin.health.utils.SMSUtils;
import com.lxin.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author: lee
 * @date: 2021-07-13
 * 发送验证码
 **/
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    private static final Logger log = LoggerFactory.getLogger(ValidateCodeController.class);

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4Order(String telephone){
        Jedis jedis = jedisPool.getResource();

        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        String codeInRedis = jedis.get(key);
        log.debug("redis中的验证码 {},{}",codeInRedis,telephone);

        // 判断redis中验证码是否为空
        if (!StringUtils.isEmpty(codeInRedis)) {
            return new Result(false,"验证码已经发送过了，请注意查收！");
        }

        // 生成验证码
        String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));

        // 发送验证码
        /*try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
        } catch (ClientException e) {
            //e.printStackTrace();
            log.error("发送验证码失败");
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/

        log.debug("发送验证码成功 {},{}",code,telephone);

        jedis.setex(key,10*60,code);
        jedis.close();

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @PostMapping("/send4Login")
    public Result send4Login(String telephone){
        Jedis jedis = jedisPool.getResource();

        String key = RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone;
        String codeInRedis = jedis.get(key);
        log.debug("redis中的验证码 {},{}",codeInRedis,telephone);

        // 判断redis中验证码是否为空
        if (!StringUtils.isEmpty(codeInRedis)) {
            return new Result(false,"验证码已经发送过了，请注意查收！");
        }

        // 生成验证码
        String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));

        // 发送验证码
        /*try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
        } catch (ClientException e) {
            //e.printStackTrace();
            log.error("发送验证码失败");
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/

        log.debug("发送验证码成功 {},{}",code,telephone);

        jedis.setex(key,10*60,code);
        jedis.close();

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
