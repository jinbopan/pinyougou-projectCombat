package cn.itcast.solr;

import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    //多条件分页查询
    @Test
    public void multiQuery() {
        SimpleQuery query = new SimpleQuery();

        //参数1：域名； contains表示包含词条，不会分词
        Criteria criteria1 = new Criteria("item_title").contains("手机");
        query.addCriteria(criteria1);

        //价格小于等于4000
        Criteria criteria2 = new Criteria("item_price").lessThanEqual(3000);
        query.addCriteria(criteria2);

        //起始索引号 = （页号-1）*页大小
        query.setOffset(0);
        //页大小
        query.setRows(10);

        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        showPage(scoredPage);
    }

    //分页查询
    @Test
    public void queryForPage() {
        SimpleQuery query = new SimpleQuery("item_title:手机");

        //起始索引号 = （页号-1）*页大小
        query.setOffset(0);
        //页大小
        query.setRows(10);

        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        showPage(scoredPage);
    }

    /**
     * 将查询结果进行显示
     * @param scoredPage
     */
    private void showPage(ScoredPage<TbItem> scoredPage) {
        System.out.println("总记录数为：" + scoredPage.getTotalElements());
        System.out.println("总页数为：" + scoredPage.getTotalPages());
        for (TbItem item : scoredPage.getContent()) {
            System.out.println("ID = " + item.getId());
            System.out.println("title = " + item.getTitle());
            System.out.println("price = " + item.getPrice());
            System.out.println("category = " + item.getCategory());
            System.out.println("image = " + item.getImage());
        }
    }

    //根据条件删除
    @Test
    public void deleteByQuery() {
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    //根据id删除
    @Test
    public void deleteById() {
        solrTemplate.deleteById("6946605");
        solrTemplate.commit();
    }

    //新增或更新
    @Test
    public void addOrUpdate() {
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
