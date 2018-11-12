package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/pay")
@RestController
public class PayController {

    @Reference
    private OrderService orderService;

    @Reference
    private WeixinPayService weixinPayService;

    /**
     * 根据订单支付日志id（交易号）到支付系统生成支付订单并返回支付二维码地址
     * @param outTradeNo 订单支付日志id（交易号）
     * @return 包含支付二维码地址的信息
     */
    @GetMapping("/createNative")
    public Map<String, String> createNative(String outTradeNo) {
        //1、获取支付日志
        TbPayLog payLog = orderService.findPayLogByOutTradeNo(outTradeNo);
        if(payLog != null) {
            String totalFee = payLog.getTotalFee() + "";
            //2、调用支付统一下单方法生成支付订单并返回信息
            return weixinPayService.createNative(outTradeNo, totalFee);
        }

        return new HashMap<>();
    }
}
