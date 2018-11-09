package com.pinyougou.cart.controller;

import com.alibaba.fastjson.JSONArray;
import com.pinyougou.common.util.CookieUtils;
import com.pinyougou.vo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/cart")
@RestController
public class CartController {

    //在浏览器中cookie的名称
    private static final String COOKIE_CART_LIST = "PYG_CART_LIST";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 在登录或者未登录情况下加载购物车数据
     * @return 购物车列表
     */
    @GetMapping("/findCartList")
    public List<Cart> findCartList(){
        //如果在没有登录的时候；则返回的用户名为anonymousUser
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(username)) {
            List<Cart> cookie_cartList = null;
            //未登录；则从cookie加载购物车数据
            String cartListJsonStr = CookieUtils.getCookieValue(request, COOKIE_CART_LIST, true);
            if(!StringUtils.isEmpty(cartListJsonStr)){
                cookie_cartList = JSONArray.parseArray(cartListJsonStr, Cart.class);
            }

            return cookie_cartList;
        } else {
            //已登录；则从redis加载购物车数据
        }
        return null;
    }

    /**
     * 获取当前登录的用户信息
     * @return 用户信息
     */
    @GetMapping("/getUsername")
    public Map<String, Object> getUsername(){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //如果在没有登录的时候；则返回的用户名为anonymousUser
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        resultMap.put("username", username);
        return resultMap;
    }
}
