package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        Map<String, Object> resultMap = new HashMap<>();

        //创建查询条件对象
        //SimpleQuery query = new SimpleQuery();
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //设置查询关键字
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //设置高亮
        HighlightOptions highlightOptions = new HighlightOptions();

        //添加一个要高亮显示的域名
        highlightOptions.addField("item_title");
        //高亮起始标签
        highlightOptions.setSimplePrefix("<font style='color:red'>");
        //高亮结束标签
        highlightOptions.setSimplePostfix("</font>");
        query.setHighlightOptions(highlightOptions);

        //分类过滤条件查询
        if (!StringUtils.isEmpty(searchMap.get("category"))) {
            //创建过滤查询对象
            Criteria catCriteria = new Criteria("item_category").is(searchMap.get("category"));

            SimpleFilterQuery catFilterQuery = new SimpleFilterQuery(catCriteria);
            query.addFilterQuery(catFilterQuery);
        }

        //品牌过滤条件查询
        if (!StringUtils.isEmpty(searchMap.get("brand"))) {
            //创建过滤查询对象
            Criteria brandCriteria = new Criteria("item_brand").is(searchMap.get("brand"));

            SimpleFilterQuery brandFilterQuery = new SimpleFilterQuery(brandCriteria);
            query.addFilterQuery(brandFilterQuery);
        }

        //规格过滤条件查询
        if (searchMap.get("spec") != null) {

            //逐个处理每个规格
            Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
            Set<Map.Entry<String, String>> entries = specMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                //创建过滤查询对象
                Criteria specCriteria = new Criteria("item_spec_" + entry.getKey()).is(entry.getValue());
                SimpleFilterQuery specFilterQuery = new SimpleFilterQuery(specCriteria);
                query.addFilterQuery(specFilterQuery);
            }
        }

        //价格过滤条件查询
        if (!StringUtils.isEmpty(searchMap.get("price"))) {
            String[] prices = searchMap.get("price").toString().split("-");

            //创建起始价格过滤查询对象
            Criteria startCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
            SimpleFilterQuery startFilterQuery = new SimpleFilterQuery(startCriteria);
            query.addFilterQuery(startFilterQuery);

            //创建结束价格过滤查询对象
            if (!"*".equals(prices[1])) {
                Criteria endCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                SimpleFilterQuery endFilterQuery = new SimpleFilterQuery(endCriteria);
                query.addFilterQuery(endFilterQuery);
            }


        }

        //设置分页参数
        int pageNo = 1;
        if (!StringUtils.isEmpty(searchMap.get("pageNo"))) {
            pageNo = Integer.parseInt(searchMap.get("pageNo").toString());
        }
        int pageSize = 20;
        if (!StringUtils.isEmpty(searchMap.get("pageSize"))) {
            pageSize = Integer.parseInt(searchMap.get("pageSize").toString());
        }

        //起始索引号 = （当前页号-1）*页大小
        query.setOffset((pageNo - 1) * pageSize);
        //页大小
        query.setRows(pageSize);

        //设置排序
        if (!StringUtils.isEmpty(searchMap.get("sortField")) && !StringUtils.isEmpty(searchMap.get("sort"))) {
            //排序序列DESC/ASC
            String sortOrder = searchMap.get("sort").toString();

            //创建排序对象sort；参数1：排序的序列；参数2：排序的域名
            Sort sort = new Sort("DESC".equals(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC,
                    "item_" + searchMap.get("sortField"));

            query.addSort(sort);
        }


        //分页查询
        //ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //处理高亮标题
        List<HighlightEntry<TbItem>> highlighted = highlightPage.getHighlighted();
        if (highlighted != null && highlighted.size() > 0) {
            for (HighlightEntry<TbItem> entry : highlighted) {
                if (entry.getHighlights().size() > 0 && entry.getHighlights().get(0).getSnipplets() != null) {
                    //设置的是返回回来的那些商品标题
                    entry.getEntity().setTitle(entry.getHighlights().get(0).getSnipplets().get(0));
                }
            }
        }

        resultMap.put("rows", highlightPage.getContent());

        //总页数
        resultMap.put("totalPages", highlightPage.getTotalPages());
        //总记录数
        resultMap.put("total", highlightPage.getTotalElements());

        return resultMap;
    }
}
