package com.lxin.health.controller;

import com.lxin.health.constant.MessageConstant;
import com.lxin.health.entity.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lee
 * @date: 2021-07-14
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/getLoginUsername")
    public Result getLoginUsername(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
    }
}
