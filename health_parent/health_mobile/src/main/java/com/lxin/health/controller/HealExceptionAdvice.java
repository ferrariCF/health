package com.lxin.health.controller;

import com.lxin.health.entity.Result;
import com.lxin.health.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: lee
 * @date: 2021-07-03
 * 日志的打印:
 * info: 打印流程性的东西
 * debug: 记录关键的数据key 订单id
 * error: 打印异常，代替e.printStackTrace, System.out.println
 **/
@RestControllerAdvice
public class HealExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(HealExceptionAdvice.class);

    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e){
        return new Result(false,e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("发生未知异常",e);
        return new Result(false,"发知未知异常，请稍后重试");
    }
}
