package com.lxin.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxin.health.dao.UserDao;
import com.lxin.health.pojo.User;
import com.lxin.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: lee
 * @date: 2021-07-14
 **/
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
