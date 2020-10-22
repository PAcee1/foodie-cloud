package com.pacee1.order.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pacee1.enums.OrderStatusEnum;
import com.pacee1.enums.YesOrNo;
import com.pacee1.order.mapper.OrderMapperCustom;
import com.pacee1.order.mapper.OrderStatusMapper;
import com.pacee1.order.mapper.OrdersMapper;
import com.pacee1.order.pojo.OrderStatus;
import com.pacee1.order.pojo.Orders;
import com.pacee1.order.pojo.vo.MyOrdersVO;
import com.pacee1.order.pojo.vo.OrderStatusCountsVO;
import com.pacee1.order.service.center.MyOrderService;
import com.pacee1.pojo.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

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
public class MyOrderServiceImpl implements MyOrderService {

    @Autowired
    private OrderMapperCustom orderMapperCustom;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PagedGridResult queryMyOrderList(String userId, String orderStatus, Integer page, Integer pagesize) {
        PageHelper.startPage(page,pagesize);

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        if(orderStatus != null){
            paramMap.put("orderStatus",orderStatus);
        }
        List<MyOrdersVO> myOrdersVOS = orderMapperCustom.queryMyOrderList(paramMap);

        return setPagedGridResult(myOrdersVOS,page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeliverOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        orderStatus.setDeliverTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateConfirmOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeleteOrder(String orderId) {
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsDelete(YesOrNo.YES.type);
        order.setUpdatedTime(new Date());

        ordersMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Orders checkUserOrder(String userId, String orderId) {
        Orders order = new Orders();
        order.setId(orderId);
        order.setUserId(userId);

        Orders orders = ordersMapper.selectOne(order);

        return orders;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatusCountsVO queryOrderStatusCounts(String userId) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        paramMap.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        OrderStatusCountsVO countsVO = new OrderStatusCountsVO();
        // 待付款
        Integer waitPayCounts = orderMapperCustom.queryOrderCountByStatus(paramMap);
        countsVO.setWaitPayCounts(waitPayCounts);
        // 待发货
        paramMap.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        Integer waitDeliverCounts = orderMapperCustom.queryOrderCountByStatus(paramMap);
        countsVO.setWaitDeliverCounts(waitDeliverCounts);
        // 待收货
        paramMap.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        Integer waitReceiveCounts = orderMapperCustom.queryOrderCountByStatus(paramMap);
        countsVO.setWaitReceiveCounts(waitReceiveCounts);
        // 待评价
        paramMap.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        paramMap.put("isComment", YesOrNo.NO.type);
        Integer waitCommentCounts = orderMapperCustom.queryOrderCountByStatus(paramMap);
        countsVO.setWaitCommentCounts(waitCommentCounts);

        return countsVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryOrderTrend(String userId,Integer page,Integer pagesize) {
        PageHelper.startPage(page,pagesize);

        List<OrderStatus> orderStatuses = orderMapperCustom.queryOrderTrend(userId);

        return setPagedGridResult(orderStatuses,page);
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
