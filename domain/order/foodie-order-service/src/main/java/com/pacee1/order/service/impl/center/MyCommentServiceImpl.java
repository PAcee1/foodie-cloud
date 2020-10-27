package com.pacee1.order.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pacee1.enums.YesOrNo;
import com.pacee1.order.mapper.*;
import com.pacee1.order.pojo.OrderItems;
import com.pacee1.order.pojo.OrderStatus;
import com.pacee1.order.pojo.Orders;
import com.pacee1.order.pojo.bo.center.OrderItemsCommentBO;
import com.pacee1.order.service.center.MyCommentService;
import com.pacee1.pojo.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pace
 * @version v1.0
 * @Type UserServiceImpl.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@RestController
public class MyCommentServiceImpl implements MyCommentService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private Sid sid;

    // 不再使用直接注入调用，需要服务间调用
    /*@Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;*/

    // TODO 使用Feign改造
    @Resource(name = "RestTemplateLB")
    private RestTemplate restTemplate;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);

        List<OrderItems> result = orderItemsMapper.select(orderItems);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCommentList(String userId, String orderId, List<OrderItemsCommentBO> orderItemList) {
        // 1.保存商品评价
        // 循环设置id，保存到数据库
        for (OrderItemsCommentBO oic : orderItemList) {
            oic.setCommentId(sid.nextShort());
        }
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        paramMap.put("orderItemList",orderItemList);
        //itemsCommentsMapperCustom.saveCommentList(paramMap);
        // 服务间调用 TODO 使用Feign改造
        restTemplate.postForLocation("http://FOODIE-ITEM-SERVICE/item-comments-api/saveComments", paramMap);
        /*ServiceInstance choose = client.choose("FOODIE-ITEM-SERVICE");
        String target = String.format("http://%s:%s/item-comments-api/saveComments",
                choose.getHost(),
                choose.getPort());
        restTemplate.postForLocation(target,paramMap);*/

        // 2.修改订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);

        // 3.修改订单评价字段
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);
    }

    /**
     * 封装分页结果
     * @param list
     * @param page
     * @return
     */
    private PagedGridResult setPagedGridResult(List<?> list,Integer page){
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult result = new PagedGridResult();
        result.setPage(page);
        result.setRows(list);
        result.setRecords(pageInfo.getTotal());
        result.setTotal(pageInfo.getPages());

        return result;
    }
}
