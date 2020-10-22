package com.pacee1.order.mapper;

import com.pacee1.order.pojo.OrderStatus;
import com.pacee1.order.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Created by pace
 * @Date 2020/6/8 10:58
 * @Classname CategoryMapperCustom
 */
public interface OrderMapperCustom {

    List<MyOrdersVO> queryMyOrderList(Map<String, Object> paramMap);

    Integer queryOrderCountByStatus(Map<String, Object> paramMap);

    List<OrderStatus> queryOrderTrend(@Param("userId") String userId);
}
