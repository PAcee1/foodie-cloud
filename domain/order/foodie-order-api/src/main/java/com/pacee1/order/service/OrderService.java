package com.pacee1.order.service;

import com.pacee1.order.pojo.OrderStatus;
import com.pacee1.order.pojo.bo.PlaceOrderBO;
import com.pacee1.pojo.ShopcartBO;
import com.pacee1.order.pojo.bo.OrderBO;
import com.pacee1.order.pojo.vo.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 17:20
 * @Classname ItemService
 */
@FeignClient("foodie-order-service")
@RequestMapping("order-api")
public interface OrderService {

    @PostMapping("order")
    OrderVO create(@RequestBody PlaceOrderBO placeOrderBO);
    //OrderVO create(OrderBO orderBO, List<ShopcartBO> shopcartList);

    /**
     * 更新订单支付状态
     * @param merchantOrderId
     * @param type
     */
    @PutMapping("orderStatus")
    void updateOrderStatus(@RequestParam("merchantOrderId") String merchantOrderId,
                           @RequestParam("type") Integer type);

    @GetMapping("orderStatus")
    OrderStatus getOrderStatus(@RequestParam("orderId") String orderId);

    @DeleteMapping("closePendingOrders")
    void closeOrder();
}
