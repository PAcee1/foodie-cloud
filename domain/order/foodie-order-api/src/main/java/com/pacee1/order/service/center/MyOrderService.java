package com.pacee1.order.service.center;

import com.pacee1.order.pojo.Orders;
import com.pacee1.order.pojo.vo.OrderStatusCountsVO;
import com.pacee1.pojo.PagedGridResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@FeignClient("foodie-order-service")
@RequestMapping("myorder-api")
public interface MyOrderService {

    /**
     * 分页查询订单
     * @param userId
     * @param orderStatus
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("order/query")
    PagedGridResult queryMyOrderList(@RequestParam("userId") String userId,
                                     @RequestParam("orderStatus") String orderStatus,
                                     @RequestParam(value = "page",required = false) Integer page,
                                     @RequestParam(value = "pagesize",required = false) Integer pagesize);

    @PostMapping("order/delivered")
    void updateDeliverOrder(@RequestParam("orderId") String orderId);

    @PostMapping("order/received")
    void updateConfirmOrder(@RequestParam("orderId") String orderId);

    @DeleteMapping("order")
    void updateDeleteOrder(@RequestParam("orderId") String orderId);

    @GetMapping("checkUserOrder")
    Orders checkUserOrder(@RequestParam("orderId") String userId,
                          @RequestParam("orderId") String orderId);

    @GetMapping("order/counts")
    OrderStatusCountsVO queryOrderStatusCounts(String userId);

    @GetMapping("order/trend")
    PagedGridResult queryOrderTrend(@RequestParam("orderId") String userId,
                                    @RequestParam(value = "page",required = false)  Integer page,
                                    @RequestParam(value = "pagesize",required = false)  Integer pagesize);

}
