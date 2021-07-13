package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.constant.RedisMessageConstant;
import com.lxin.health.entity.Result;
import com.lxin.health.pojo.Order;
import com.lxin.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-13
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> paraMap){
        // 校验验证码
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + paraMap.get("telephone");

        String codeInRedis = jedis.get(key);

        if (StringUtils.isEmpty(codeInRedis)) {
            return new Result(false,"请重新发送验证码！");
        }

        if (!codeInRedis.equals(paraMap.get("validateCode"))) {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 验证码正确，删除redis中的验证码
        jedis.del(key);
        jedis.close();

        //设置预约类型
        paraMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        Integer id = orderService.submit(paraMap);

        return new Result(true,MessageConstant.ORDER_SUCCESS,id);
    }

    /**
     * 预约订单回显
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,String> orderInfo = orderService.findById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}
