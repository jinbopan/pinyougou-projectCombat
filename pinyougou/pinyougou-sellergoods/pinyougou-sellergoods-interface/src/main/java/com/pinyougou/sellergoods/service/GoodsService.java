package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;

public interface GoodsService extends BaseService<TbGoods> {

    PageResult search(Integer page, Integer rows, TbGoods goods);

    /**
     * 接收商品基本、描述、sku列表并保存商品基本、描述信息sku列表
     * @param goods 商品vo{TbGoods,TbGoodsDesc,List<TbItem>}
     */
    void addGoods(Goods goods);

    /**
     * 根据商品spu id查询商品信息（基本、描述、sku列表）
     * @param id 商品spu id
     * @return 商品信息（基本、描述、sku列表）
     */
    Goods findGoodsById(Long id);

    /**
     * 根据商品spu id更新商品基本、描述、sku列表
     * @param goods 商品信息（基本、描述、sku列表）
     */
    void updateGoods(Goods goods);

    /**
     *根据商品spu id集合更新这些商品spu的审核状态
     * @param ids 商品spu id集合
     * @param status 审核状态
     */
    void updateStatus(Long[] ids, String status);

    /**
     * 更新spu id数组对应的那些商品基本信息的删除状态为1
     * @param ids spu id数组
     */
    void deleteGoodsByIds(Long[] ids);
}