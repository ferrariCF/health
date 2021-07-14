package com.lxin.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.pojo.Permission;
import com.lxin.health.pojo.Role;
import com.lxin.health.pojo.User;
import com.lxin.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: lee
 * @date: 2021-07-14
 **/
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (null != user) {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            Set<Role> userRoles = user.getRoles();

            if (userRoles != null) {
                userRoles.forEach(role -> {
                    authorityList.add(new SimpleGrantedAuthority(role.getKeyword()));
                    Set<Permission> permissions = role.getPermissions();
                    if (null != permissions) {
                        permissions.forEach(permission -> {
                            authorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
                        });
                    }
                });
            }
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorityList);
            return userDetails;
        }
        return null;
    }
}
