package com.pacee1.order.service.center;

import com.pacee1.order.pojo.OrderItems;
import com.pacee1.order.pojo.bo.center.OrderItemsCommentBO;
import com.pacee1.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@RestController("order-comments-api")
public interface MyCommentService {

    @GetMapping("pendingComment")
    List<OrderItems> queryPendingComment(@RequestParam("orderId") String orderId);

    @PostMapping("comments")
    void saveCommentList(@RequestParam("userId") String userId,
                         @RequestParam("orderId") String orderId,
                         @RequestBody List<OrderItemsCommentBO> orderItemList);

    // 移到了Item微服务，ItemCommentsService
    /*@GetMapping("pagedComments")
    PagedGridResult queryCommentList(@RequestParam("userId") String userId,
                                     @RequestParam(value = "page",required = false) Integer page,
                                     @RequestParam(value = "pagesize",required = false) Integer pagesize);*/
}
