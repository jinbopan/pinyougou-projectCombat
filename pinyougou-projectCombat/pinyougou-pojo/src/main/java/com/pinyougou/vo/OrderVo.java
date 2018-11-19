package com.pinyougou.vo;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.Date;

public class OrderVo implements Serializable {

    private TbOrder tbOrder;
    private TbOrderItem tbOrderItem;
    private TbItem tbItem;
    private TbGoods tbGoods;

    public TbOrder getTbOrder() {
        return tbOrder;
    }

    public void setTbOrder(TbOrder tbOrder) {
        this.tbOrder = tbOrder;
    }

    public TbOrderItem getTbOrderItem() {
        return tbOrderItem;
    }

    public void setTbOrderItem(TbOrderItem tbOrderItem) {
        this.tbOrderItem = tbOrderItem;
    }

    public TbItem getTbItem() {
        return tbItem;
    }

    public void setTbItem(TbItem tbItem) {
        this.tbItem = tbItem;
    }

    public TbGoods getTbGoods() {
        return tbGoods;
    }

    public void setTbGoods(TbGoods tbGoods) {
        this.tbGoods = tbGoods;
    }
}
