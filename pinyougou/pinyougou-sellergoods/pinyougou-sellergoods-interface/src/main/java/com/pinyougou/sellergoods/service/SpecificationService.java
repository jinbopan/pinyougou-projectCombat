package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Specification;

public interface SpecificationService extends BaseService<TbSpecification> {

    PageResult search(Integer page, Integer rows, TbSpecification specification);

    /**
     * 保存规格及其选项列表到数据库中
     * @param specification 规格信息（规格及选项列表）；如：
     *                  {"specificationOptionList":[{"optionName":"蓝色","orders":"1"}],"specification":{"specName":"颜色"}}
     */
    void add(Specification specification);
}