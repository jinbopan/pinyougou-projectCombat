package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbGoods goods) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(goods.get***())){
            criteria.andLike("***", "%" + goods.get***() + "%");
        }*/

        List<TbGoods> list = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void addGoods(Goods goods) {
        //1、保存商品spu基本信息
        add(goods.getGoods());

        //2、保存商品spu描述信息
        //商品spuid在保存完基本信息后会回填；再设置给商品描述信息的主键
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insertSelective(goods.getGoodsDesc());

        //3、保存商品sku列表
        saveItemList(goods);
    }

    /**
     * 保存商品sku列表
     * @param goods 商品信息（基本、描述、sku列表）
     */
    private void saveItemList(Goods goods) {
        if("1".equals(goods.getGoods().getIsEnableSpec())) {
            //启用规格，则需要处理前端传递过滤的sku
            if (goods.getItemList() != null && goods.getItemList().size() > 0) {
                for (TbItem item : goods.getItemList()) {

                    //sku标题 = spu标题+该商品的所有规格选项的值
                    String title = goods.getGoods().getGoodsName();

                    //获取前端传递的规格及其选项
                    Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }

                    item.setTitle(title);

                    setItemValue(item, goods);

                    //保存sku
                    itemMapper.insertSelective(item);
                }
            }
        } else {
            //不启用规格；可以根据spu基本信息生成一条sku商品数据
            TbItem tbItem = new TbItem();

            tbItem.setTitle(goods.getGoods().getGoodsName());
            tbItem.setPrice(goods.getGoods().getPrice());
            //默认库存
            tbItem.setNum(9999);
            //默认不启用
            tbItem.setStatus("0");
            //因为只有一个sku所以默认
            tbItem.setIsDefault("1");

            setItemValue(tbItem, goods);

            itemMapper.insertSelective(tbItem);

        }
    }

    /**
     * 根据商品信息设置sku
     * @param item sku商品
     * @param goods spu商品信息
     */
    private void setItemValue(TbItem item, Goods goods) {
        //根据品牌id查询品牌
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());

        //从spu中获取第一个图片
        if (!StringUtils.isEmpty(goods.getGoodsDesc().getItemImages())) {
            List<Map> list = JSONArray.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
            item.setImage(list.get(0).get("url").toString());
        }
        item.setGoodsId(goods.getGoods().getId());

        //商品分类；来自spu的第3级分类的中文名称
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategoryid(itemCat.getId());
        item.setCategory(itemCat.getName());

        item.setCreateTime(new Date());
        item.setUpdateTime(item.getCreateTime());

        //商家id
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSellerId(seller.getSellerId());
        item.setSeller(seller.getName());
    }
}
