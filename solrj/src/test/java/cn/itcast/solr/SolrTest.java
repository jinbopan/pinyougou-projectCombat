package cn.itcast.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SolrTest {

    //创建httpSolrServer
    private HttpSolrServer httpSolrServer;

    @Before
    public void setup(){
        //solr服务器地址
        String url = "http://127.0.0.1:8080/solr";
        //实例化httpSolrServer
        httpSolrServer = new HttpSolrServer(url);

    }

    /**
     * 新增或者更新
     * 如果存在则更新，不存在则新增
     */
    @Test
    public void addOrUpdate() throws Exception {

        //创建文档
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        //添加文档域；参数1：在solr的schema.xml文件中配置的域名，参数2：域值
        solrInputDocument.addField("id", "123");
        solrInputDocument.addField("item_title", "333 荣耀畅玩8C 全网通标配版 4GB+32GB 幻夜黑 移动联通电信4G全面屏手机 双卡双待");
        solrInputDocument.addField("item_catalog_name", "手机");
        solrInputDocument.addField("item_price", 1099);
        solrInputDocument.addField("item_image", "https://item.jd.com/100000650837.html");

        //新增或者更新；
        httpSolrServer.add(solrInputDocument);

        //提交
        httpSolrServer.commit();
    }

    /**
     * 根据主键删除
     */
    @Test
    public void deleteById() throws Exception {
        httpSolrServer.deleteById("123");
        httpSolrServer.commit();
    }

    /**
     * 根据其它条件删除
     */
    @Test
    public void deleteByQuery() throws Exception {
        //httpSolrServer.deleteByQuery("item_title:荣耀");
        httpSolrServer.deleteByQuery("*:*");
        httpSolrServer.commit();
    }
    /**
     * 根据条件查询
     */
    @Test
    public void query() throws Exception {
        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();

        solrQuery.setQuery("item_title:荣耀 AND item_price:{1000 TO 2000]");

        //查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);


        System.out.println("符合本次查询的总记录数：" + queryResponse.getResults().getNumFound());

        //输出所有的查询结果
        SolrDocumentList results = queryResponse.getResults();

        if (results != null && results.size() > 0) {
            for (SolrDocument solrDocument : results) {
                System.out.println("id = " + solrDocument.get("id"));
                System.out.println("item_title = " + solrDocument.get("item_title"));
                System.out.println("item_price = " + solrDocument.get("item_price"));
                System.out.println("item_image = " + solrDocument.get("item_image"));
            }
        }


    }

    /**
     * 根据条件查询
     */
    @Test
    public void slorCorequery() throws Exception {

        httpSolrServer = new HttpSolrServer("http://127.0.0.1:8080/solr/collection2");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();

        solrQuery.setQuery("item_title:荣耀 AND item_price:{1000 TO 2000]");

        //查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);


        System.out.println("符合本次查询的总记录数：" + queryResponse.getResults().getNumFound());

        //输出所有的查询结果
        SolrDocumentList results = queryResponse.getResults();

        if (results != null && results.size() > 0) {
            for (SolrDocument solrDocument : results) {
                System.out.println("id = " + solrDocument.get("id"));
                System.out.println("item_title = " + solrDocument.get("item_title"));
                System.out.println("item_price = " + solrDocument.get("item_price"));
                System.out.println("item_image = " + solrDocument.get("item_image"));
            }
        }


    }

    /**
     * 根据条件查询
     */
    @Test
    public void HighLightQuery() throws Exception {
        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();

        solrQuery.setQuery("item_title:荣耀 AND item_price:{1000 TO 2000]");

        //设置高亮
        solrQuery.setHighlight(true);
        //设置高亮域
        solrQuery.addHighlightField("item_title");
        //设置高亮起始标签
        solrQuery.setHighlightSimplePre("<font style='color:red'>");
        //设置高亮结束标签
        solrQuery.setHighlightSimplePost("</font>");

        //查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);


        System.out.println("符合本次查询的总记录数：" + queryResponse.getResults().getNumFound());

        //输出所有的查询结果
        SolrDocumentList results = queryResponse.getResults();

        /**
         * 获取高亮标题;结构如下：
         * "highlighting": {
         *     "123": {
         *       "item_title": [
         *         "222 <em>荣耀</em>畅玩8C 全网通标配版 4GB+32GB 幻夜黑 移动联通电信4G全面屏手机 双卡双待"
         *       ]
         *     }
         *   }
         */
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        if (results != null && results.size() > 0) {
            for (SolrDocument solrDocument : results) {
                System.out.println("id = " + solrDocument.get("id"));
                System.out.println("item_title = " + solrDocument.get("item_title"));

                //高亮标题
                String title = highlighting.get(solrDocument.get("id").toString()).get("item_title").get(0);

                System.out.println("item_title高亮标题为： " + title);
                System.out.println("item_catalog_name = " + solrDocument.get("item_catalog_name"));
                System.out.println("item_price = " + solrDocument.get("item_price"));
                System.out.println("item_image = " + solrDocument.get("item_image"));
            }
        }


    }
}
