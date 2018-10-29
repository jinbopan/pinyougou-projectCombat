package com.pinyougou.content.service;

import com.pinyougou.pojo.TbContent;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface ContentService extends BaseService<TbContent> {

    PageResult search(Integer page, Integer rows, TbContent content);

    /**
     * 根据内容分类（轮播广告）并且有效的内容数据按照排序字段降序排序
     * @param categoryId 内容分类Id
     * @return 内容列表
     */
    List<TbContent> findContentListByCategoryId(Long categoryId);
}