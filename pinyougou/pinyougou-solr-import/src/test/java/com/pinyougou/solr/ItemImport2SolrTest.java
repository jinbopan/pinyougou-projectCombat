package com.pinyougou.solr;

import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class ItemImport2SolrTest {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 将商品sku表tb_item的所有已启用的数据导入到solr中
     */
    @Test
    public void test(){
        //1、查询数据库中已启用的sku商品列表
        TbItem param = new TbItem();
        //已启用
        param.setStatus("1");
        List<TbItem> itemList = itemMapper.select(param);

        //2、逐个遍历每个商品，将spec转换到specMap中
        for (TbItem item : itemList) {
            Map map = JSONObject.parseObject(item.getSpec(), Map.class);
            item.setSpecMap(map);
        }

        //3、批量导入到solr中
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }
}
