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

                //根据品牌id查询品牌
                TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
                item.setBrand(brand.getName());

                //从spu中获取第一个图片
                if(!StringUtils.isEmpty(goods.getGoodsDesc().getItemImages())) {
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

                //保存sku
                itemMapper.insertSelective(item);
            }
        }
    }
}
