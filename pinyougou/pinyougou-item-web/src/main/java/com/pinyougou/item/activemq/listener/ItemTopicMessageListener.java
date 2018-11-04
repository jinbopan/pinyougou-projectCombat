package com.pinyougou.item.activemq.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 在运营商管理系统审核商品之后需要发送商品变更的消息（商品spuid集合)到MQ，
 * 详情系统需要接收消息并根据spu id查询商品信息利用Freemarker生成静态页面到指定路径下
 */
public class ItemTopicMessageListener extends AbstractAdaptableMessageListener {


    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Reference
    private ItemCatService itemCatService;

    @Reference
    private GoodsService goodsService;

    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        //1、接收消息（商品spu id数组）
        ObjectMessage objectMessage = (ObjectMessage) message;
        Long[] ids = (Long[]) objectMessage.getObject();

        //2、针对每一个spu id生成对应的静态页面
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                genHtml(id);
            }
        }
    }

    /**
     * 需要根据商品spu id查询商品信息（分类，基本、描述、sku列表）
     * 再获取到freemarker商品item.ftl模版并输出html页面到一个指定路径
     * @param goodsId 商品spu id
     */
    private void genHtml(Long goodsId) {
        try {
            //获取freemarker配置对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();

            //1、获取模版
            Template template = configuration.getTemplate("item.ftl");

            //2、数据
            Map<String, Object> dataModel = new HashMap<>();

            //根据商品spu id查询商品信息（基本、描述、sku列表(已启用；并且按照是否默认排序降序排序)）
            //返回的sku列表要按照是否默认降序排序是因为在详情页面刚进入的时候应该要默认显示一个sku
            Goods goods = goodsService.findGoodsByIdAndStatus(goodsId, "1");

            //根据商品id查询商品基本信息获取3级商品分类id;再根据分类id查询分类
            //itemCat1 1级商品分类中文名称
            TbItemCat itemCat1 = itemCatService.findOne(goods.getGoods().getCategory1Id());
            dataModel.put("itemCat1", itemCat1.getName());

            //itemCat2 2级商品分类中文名称
            TbItemCat itemCat2 = itemCatService.findOne(goods.getGoods().getCategory2Id());
            dataModel.put("itemCat2", itemCat2.getName());

            //itemCat3 3级商品分类中文名称
            TbItemCat itemCat3 = itemCatService.findOne(goods.getGoods().getCategory3Id());
            dataModel.put("itemCat3", itemCat3.getName());

            //goodsDesc 商品描述信息
            dataModel.put("goodsDesc", goods.getGoodsDesc());
            //goods 商品基本信息
            dataModel.put("goods", goods.getGoods());
            //itemList 商品sku列表
            dataModel.put("itemList", goods.getItemList());

            //输出媒介
            FileWriter fileWriter = new FileWriter(ITEM_HTML_PATH + goodsId + ".html");

            //3、输出
            template.process(dataModel, fileWriter);

            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
