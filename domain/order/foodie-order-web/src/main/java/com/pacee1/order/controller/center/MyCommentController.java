package com.pacee1.order.controller.center;

import com.pacee1.item.pojo.ItemsSpec;
import com.pacee1.order.pojo.OrderItems;
import com.pacee1.order.pojo.Orders;
import com.pacee1.order.pojo.bo.center.OrderItemsCommentBO;
import com.pacee1.order.service.center.MyCommentService;
import com.pacee1.order.service.center.MyOrderService;
import com.pacee1.pojo.PagedGridResult;
import com.pacee1.pojo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/16 15:35
 * @Classname MyOrderController
 */
@RestController
@RequestMapping("mycomments")
@Api(value = "我的评价接口",tags = "我的订单接口")
public class MyCommentController {

    @Autowired
    private MyOrderService myOrderService;
    @Autowired
    private MyCommentService myCommentService;

    // TODO 使用Feign改造
    /*@Autowired
    private LoadBalancerClient client;*/
    @Resource(name = "RestTemplateLB")
    private RestTemplate restTemplate;

    @PostMapping("/pending")
    @ApiOperation(value = "查询评价商品",notes = "查询评价商品")
    public ResponseResult pending(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId",value = "订单id",required = true)
            @RequestParam String orderId){
        // 校验用户订单是否匹配
        Orders orders = myOrderService.checkUserOrder(userId, orderId);
        if(orders == null){
            return ResponseResult.errorMsg("非法请求");
        }

        // 校验订单是否有评价，已经评价过的返回错误标识
        if(orders.getIsComment() != 0){
            return ResponseResult.errorMsg("订单已评价过，不可再次评价");
        }

        // 查询待评价的商品列表返回
        List<OrderItems> orderItems = myCommentService.queryPendingComment(orderId);

        return ResponseResult.ok(orderItems);
    }

    @PostMapping("/saveList")
    @ApiOperation(value = "保存商品评价",notes = "保存商品评价")
    public ResponseResult saveList(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId",value = "订单id",required = true)
            @RequestParam String orderId,
            @ApiParam(name = "orderItemList",value = "订单商品信息",required = true)
            @RequestBody List<OrderItemsCommentBO> orderItemList){
        // 校验用户订单是否匹配
        Orders orders = myOrderService.checkUserOrder(userId, orderId);
        if(orders == null){
            return ResponseResult.errorMsg("非法请求");
        }

        // 校验订单是否有评价，已经评价过的返回错误标识
        if(orders.getIsComment() != 0){
            return ResponseResult.errorMsg("订单已评价过，不可再次评价");
        }

        // 保存评价
        myCommentService.saveCommentList(userId,orderId,orderItemList);

        return ResponseResult.ok();
    }

    @PostMapping("/query")
    @ApiOperation(value = "查询评价列表",notes = "查询评价列表")
    public ResponseResult query(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "page",value = "当前页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(userId == null){
            return ResponseResult.errorMsg("用户不存在");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }

        //PagedGridResult result = myCommentService.queryCommentList(userId, page, pageSize);
        // 服务间调用 TODO 使用Feign改造
        //ServiceInstance choose = client.choose("FOODIE-ITEM-SERVICE");
        String target = String.format("http://FOODIE-ITEM-SERVICE/item-comments-api/myComments" +
                        "?userId=%s&page=%s&pageSize=%s",
                userId,
                page,
                pageSize);
        PagedGridResult result = restTemplate.getForObject(target, PagedGridResult.class);


        return ResponseResult.ok(result);
    }

}
