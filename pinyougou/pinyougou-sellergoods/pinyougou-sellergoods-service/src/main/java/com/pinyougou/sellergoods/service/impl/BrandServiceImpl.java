package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

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

    @Override
    public PageResult search(TbBrand brand, Integer pageNo, Integer rows) {

        //设置分页
        PageHelper.startPage(pageNo, rows);

        //创建查询对象；本次要操作的实体类
        Example example = new Example(TbBrand.class);

        //创建查询条件对象 -- 构建where子句
        Example.Criteria criteria = example.createCriteria();

        //根据首字母
        //-->brand.getFirstChar() != null && !"".equals(brand.getFirstChar())
        if(!StringUtils.isEmpty(brand.getFirstChar())){
            //参数1：实体类中的属性名称，参数2：查询条件值  ---》 first_char = L
            criteria.andEqualTo("firstChar", brand.getFirstChar());
        }
        //根据品牌名称
        if(!StringUtils.isEmpty(brand.getName())){
            //模糊查询 ---> name like %a%
            criteria.andLike("name", "%" + brand.getName() + "%");
        }

        //1、根据条件查询
        List<TbBrand> list = brandMapper.selectByExample(example);

        //转换为分页信息
        PageInfo<TbBrand> pageInfo = new PageInfo<>(list);

        //2、返回分页对象
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<Map<String, String>> selectOptionList() {
        return brandMapper.selectOptionList();
    }
}
