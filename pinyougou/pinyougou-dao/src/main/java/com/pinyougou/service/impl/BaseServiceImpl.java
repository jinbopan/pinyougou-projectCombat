package com.pinyougou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    //泛型依赖注入；在spring 4.x版本之后才可以使用
    @Autowired
    private Mapper<T> mapper;

    /**
     * 如果早期的spring 可以如此使用

    protected void setMapper(Mapper<T> mapper){
        this.mapper = mapper;
    }*/

    @Override
    public T findOne(Serializable id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> findByWhere(T t) {
        return mapper.select(t);
    }

    @Override
    public PageResult findPage(Integer pageNo, Integer rows) {
        //设置分页
        PageHelper.startPage(pageNo, rows);

        //查询;该list的具体类型为Page,page又继承ArrayList
        List<T> list = mapper.selectAll();

        //转换为分页信息对象
        PageInfo<T> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public PageResult findPage(Integer pageNo, Integer rows, T t) {
        //设置分页
        PageHelper.startPage(pageNo, rows);

        //查询;该list的具体类型为Page,page又继承ArrayList
        List<T> list = mapper.select(t);

        //转换为分页信息对象
        PageInfo<T> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void add(T t) {
        //选择性新增；如果对象中的属性有值的话，才会出现在语句中；如：insert into table(id, name) values(?, ?)
        mapper.insertSelective(t);
    }

    @Override
    public void update(T t) {
        //选择性更新；与新增类似
        //主键：就是在实体类中的属性上面添加了@Id的注解
        mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void deleteByIds(Serializable[] ids) {
        if (ids != null && ids.length > 0) {
            for (Serializable id : ids) {
                mapper.deleteByPrimaryKey(id);
            }
        }
    }
}
