package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/brand")
//@Controller
@RestController //组合注解 Controller 和 ResponseBody；类上面的该注解对所有方法生效
public class BrandController {

    //从注册中心引入服务代理对象
    @Reference
    private BrandService brandService;

    /**
     * 查询所有品牌列表
     * @return 品牌列表
     */
    //@RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @GetMapping("/findAll")
    //@ResponseBody
    public List<TbBrand> findAll(){
        return brandService.queryAll();
    }
}
