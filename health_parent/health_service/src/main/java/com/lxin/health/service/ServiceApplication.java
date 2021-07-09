package com.lxin.health.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author: lee
 * @date: 2021-07-01
 **/
public class ServiceApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:applicationContext-service.xml");
        System.in.read();
    }
}
