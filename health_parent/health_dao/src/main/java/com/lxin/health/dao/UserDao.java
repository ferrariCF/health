package com.lxin.health.dao;

import com.lxin.health.pojo.User;

/**
 * @author: lee
 * @date: 2021-07-14
 **/
public interface UserDao {
    User findByUsername(String username);
}
