package com.pinyougou.search.activemq.listener;

import com.alibaba.fastjson.JSONArray;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.List;

/**
 * 在运营商管理系统审核商品之后需要发送商品变更的消息（商品sku列表)到MQ，
 * 搜索系统需要接收消息并将sku列表更新到solr中；
 */
public class ItemMessageListener extends AbstractAdaptableMessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        try {
            //1、接收并转换消息为itemList
            TextMessage textMessage = (TextMessage) message;
            List<TbItem> itemList = JSONArray.parseArray(textMessage.getText(), TbItem.class);

            //2、更新列表数据到solr中
            itemSearchService.importItemList(itemList);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
