package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//service来自dubbo；主要将该业务对象暴露到注册中心
@Service(interfaceClass = BrandService.class)
public class BrandServiceImpl extends BaseServiceImpl<TbBrand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /** 如果早期的spring则可以如此使用
    @Autowired
    public void setBrandMapper(BrandMapper brandMapper){
        super.setMapper(brandMapper);
        this.brandMapper = brandMapper;
    }*/

    @Override
    public List<TbBrand> queryAll() {
        return brandMapper.queryAll();
    }

    @Override
    public List<TbBrand> testPage(Integer page, Integer rows) {
        //设置分页；只对紧接着的查询语句生效
        PageHelper.startPage(page, rows);

        return brandMapper.selectAll();
    }
}
