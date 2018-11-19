package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderItemService;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.vo.OrderVo;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Reference(timeout = 200000)
    private OrderItemService orderItemService;
    @Reference(timeout = 200000)
    private OrderService orderService;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findAll")
    public List<TbOrderItem> findAll() {
        return orderItemService.findAll();
    }

    /**
     * 从订单表里查询所有未过期的订单或不是未付款但不管理是否过期的订单
     * @return
    */
    @RequestMapping("/findPage")
    public Map<String, Object> findPage(@RequestBody Map<String, Object> searchMap) {
        /*//设置允许某个域名的跨域请求响应
        response.setHeader("Access-Control-Allow-Origin", "http://cart.pinyougou.com");
        //设置允许接收客户端cookie和响应cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");*/
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        searchMap.put("userId", userId);
        Map<String, Object> resultMap = orderService.findOrder(searchMap);
        return resultMap;
    }

    @PostMapping("/add")
    public Result add(@RequestBody TbOrderItem orderItem) {
        try {
            orderItemService.add(orderItem);
            return Result.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("增加失败");
    }

    @GetMapping("/findOne")
    public TbOrderItem findOne(Long id) {
        return orderItemService.findOne(id);
    }

    @PostMapping("/update")
    public Result update(@RequestBody TbOrderItem orderItem) {
        try {
            orderItemService.update(orderItem);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    @GetMapping("/deleteMyOrder")
    public Result delete(Long[] orderItemIds) {
        try {
            orderService.deleteByOrderIds(orderItemIds);
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 分页查询列表
     * @param orderItem 查询条件
     * @param page 页号
     * @param rows 每页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody  TbOrderItem orderItem, @RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return orderItemService.search(page, rows, orderItem);
    }

    /**
     * 订单详情
     */
    @RequestMapping("/orderDetail")
    public OrderVo orderDetail(Long orderItemId){
       /* //设置允许某个域名的跨域请求响应
        response.setHeader("Access-Control-Allow-Origin", "http://cart.pinyougou.com");
        //设置允许接收客户端cookie和响应cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");*/
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderVo tbOrder = orderService.findOrderDetail(orderItemId, userId);
        return tbOrder;
    }
}
