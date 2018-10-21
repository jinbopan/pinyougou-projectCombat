package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSpecification;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SpecificationMapper extends Mapper<TbSpecification> {

    /**
     * 查询规格列表；结构如：[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
     * @return 规格列表
     */
    List<Map<String, String>> selectOptionList();
}
