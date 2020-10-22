package com.pacee1.order.controller.center;

import com.pacee1.order.pojo.Orders;
import com.pacee1.order.pojo.vo.OrderStatusCountsVO;
import com.pacee1.order.service.center.MyOrderService;
import com.pacee1.pojo.PagedGridResult;
import com.pacee1.pojo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Created by pace
 * @Date 2020/6/16 15:35
 * @Classname MyOrderController
 */
@RestController
@RequestMapping("myorders")
@Api(value = "我的订单接口",tags = "我的订单接口")
public class MyOrderController {

    @Autowired
    private MyOrderService myOrderService;

    @PostMapping("/query")
    @ApiOperation(value = "查询订单列表",notes = "查询订单列表")
    public ResponseResult query(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus",value = "订单状态",required = false)
            @RequestParam String orderStatus,
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

        PagedGridResult result = myOrderService.queryMyOrderList(userId, orderStatus, page, pageSize);

        return ResponseResult.ok(result);
    }

    // 单独接口，没有前端页面点击，需要使用postman等工具调用
    @GetMapping("/deliver")
    @ApiOperation(value = "商家发货接口",notes = "商家发货接口")
    public ResponseResult deliver(
            @ApiParam(name = "orderId",value = "订单编号",required = true)
            @RequestParam String orderId){

        myOrderService.updateDeliverOrder(orderId);

        return ResponseResult.ok();
    }

    @PostMapping("/confirmReceive")
    @ApiOperation(value = "用户确认收货接口",notes = "用户确认收货接口")
    public ResponseResult confirmReceive(
            @ApiParam(name = "orderId",value = "订单编号",required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){
        // 校验订单与用户是否匹配
        Orders orders = myOrderService.checkUserOrder(userId, orderId);
        if(orders == null){
            return ResponseResult.errorMsg("非法请求");
        }

        myOrderService.updateConfirmOrder(orderId);

        return ResponseResult.ok();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "用户删除订单接口",notes = "用户删除订单接口")
    public ResponseResult delete(
            @ApiParam(name = "orderId",value = "订单编号",required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){
        // 校验订单与用户是否匹配
        Orders orders = myOrderService.checkUserOrder(userId, orderId);
        if(orders == null){
            return ResponseResult.errorMsg("非法请求");
        }

        myOrderService.updateDeleteOrder(orderId);

        return ResponseResult.ok();
    }

    @PostMapping("/statusCounts")
    @ApiOperation(value = "订单状态查询接口",notes = "订单状态查询接口")
    public ResponseResult statusCounts(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            return ResponseResult.errorMsg("用户未登录");
        }

        OrderStatusCountsVO countsVO = myOrderService.queryOrderStatusCounts(userId);

        return ResponseResult.ok(countsVO);
    }

    @PostMapping("/trend")
    @ApiOperation(value = "查询订单动向列表",notes = "查询订单动向列表")
    public ResponseResult trend(
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

        PagedGridResult result = myOrderService.queryOrderTrend(userId, page, pageSize);

        return ResponseResult.ok(result);
    }
}
