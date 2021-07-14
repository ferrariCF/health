package com.lxin.health.service;

import com.lxin.health.pojo.User;

/**
 * @author: lee
 * @date: 2021-07-14
 **/
public interface UserService {
    User findByUsername(String username);
}
