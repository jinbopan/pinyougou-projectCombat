package cn.itcast.solr;

import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    //新增或更新
    @Test
    public void addOrUpdate(){
        TbItem item = new TbItem();
        item.setId(6946605L);
        item.setTitle("222 华为 HUAWEI P20 AI智慧徕卡双摄全面屏游戏手机 6GB+64GB 亮黑色 全网通移动联通电信4G手机 双卡双待");
        item.setPrice(new BigDecimal(3388));
        item.setImage("https://item.jd.com/6946605.html");
        item.setCategory("手机");

        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }
}
