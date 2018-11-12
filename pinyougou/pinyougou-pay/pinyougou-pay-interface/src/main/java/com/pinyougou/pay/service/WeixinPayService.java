package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /**
     * 根据订单支付日志id（交易号）到支付系统生成支付订单并返回支付二维码地址
     * @param outTradeNo 订单支付日志id（交易号）
     * @param totalFee 订单支付总金额
     * @return 包含支付二维码地址的信息
     */
    Map<String, String> createNative(String outTradeNo, String totalFee);
}
