package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService extends BaseService<TbTypeTemplate> {

    PageResult search(Integer page, Integer rows, TbTypeTemplate typeTemplate);

    /**
     * 根据分类模版id查询该分类模版对应的规格及其规格选项列表
     * [{"id":27,"text":"网络","options":[{规格选项},...]},{"id":32,"text":"机身内存","options":[{规格选项},...]}]
     * @param id 分类模版id
     * @return 规格及其规格选项列表
     */
    List<Map> findSpecList(Long id);
}