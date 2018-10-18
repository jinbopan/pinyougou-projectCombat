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
     * 根据分页条件查询，查询第1页每页5条品牌列表
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    @GetMapping("/testPage")
    public List<TbBrand> testPage(@RequestParam(value="page", defaultValue = "1")Integer page,
                                  @RequestParam(value="rows", defaultValue = "10")Integer rows){
        return brandService.testPage(page, rows);
    }

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
