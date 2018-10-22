package com.pinyougou.shop.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {

    /**
     * 根据用户输入的用户名进行授权
     * @param username 用户输入的用户名
     * @return security需要的用户信息对象
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //角色权限集合（本来应该根据用户名到数据库中查询的，但是我们的系统没有角色表）
        List<GrantedAuthority> authorities = new ArrayList<>();
        //指定一个角色叫ROLE_SELLER
        authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //任意前端输入用户名；只要密码为123456的都认证通过
        return new User(username, "123456", authorities);
    }
}
