package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface BrandService extends BaseService<TbBrand> {
    /**
     * 查询所有品牌列表
     * @return 品牌列表
     */
    List<TbBrand> queryAll();

    /**
     * 根据分页条件查询，查询第1页每页5条品牌列表
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    List<TbBrand> testPage(Integer page, Integer rows);

    /**
     * 根据分页条件查询
     * @param pageNo 页号
     * @param rows 页大小
     * @return 分页对象
     */
    PageResult search(TbBrand brand, Integer pageNo, Integer rows);
}
