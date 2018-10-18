package com.pinyougou.service;

import com.pinyougou.vo.PageResult;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

    //根据主键查询；在java中主键类型都必须要实现Serializable
    T findOne(Serializable id);

    //查询全部
    List<T> findAll();

    //根据条件查询
    List<T> findByWhere(T t);

    //根据分页（页号，页大小）查询;返回一个分页对象（总记录数，列表）
    PageResult findPage(Integer pageNo, Integer rows);

    //根据条件分页查询
    PageResult findPage(Integer pageNo, Integer rows, T t);

    //新增
    void add(T t);

    //根据主键修改
    void update(T t);

    //批量删除
    void deleteByIds(Serializable[] ids);
}
