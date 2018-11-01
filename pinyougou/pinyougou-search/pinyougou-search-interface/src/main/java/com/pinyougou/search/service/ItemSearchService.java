package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    /**
     * 根据搜索条件查询solr中商品数据
     * @param searchMap 搜索条件
     * @return 查询结果
     */
    Map<String, Object> search(Map<String, Object> searchMap);

    /**
     * 更新solr中的商品数据
     * @param itemList 商品列表
     */
    void importItemList(List<TbItem> itemList);

    /**
     * 在搜索系统系统接收商品spu id数组；并且根据这些spu id删除在solr中对应的sku商品
     * @param goodsIds 商品spu id数组
     */
    void deleteItemByGoodsIds(List<Long> goodsIds);
}
