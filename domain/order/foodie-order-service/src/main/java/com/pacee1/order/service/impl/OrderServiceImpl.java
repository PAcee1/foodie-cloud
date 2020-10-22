package com.pacee1.order.service.impl;

import com.pacee1.enums.OrderStatusEnum;
import com.pacee1.enums.YesOrNo;
import com.pacee1.item.pojo.Items;
import com.pacee1.item.pojo.ItemsImg;
import com.pacee1.item.pojo.ItemsSpec;
import com.pacee1.order.mapper.OrderItemsMapper;
import com.pacee1.order.mapper.OrderStatusMapper;
import com.pacee1.order.mapper.OrdersMapper;
import com.pacee1.order.pojo.OrderItems;
import com.pacee1.order.pojo.OrderStatus;
import com.pacee1.order.pojo.Orders;
import com.pacee1.order.pojo.bo.PlaceOrderBO;
import com.pacee1.pojo.ShopcartBO;
import com.pacee1.order.pojo.bo.OrderBO;
import com.pacee1.order.pojo.vo.MerchantOrderVO;
import com.pacee1.order.pojo.vo.OrderVO;
import com.pacee1.order.service.OrderService;
import com.pacee1.user.pojo.UserAddress;
import com.pacee1.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type UserServiceImpl.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@RestController
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Sid sid;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    // 不再使用直接注入调用，需要服务间调用
    /*@Autowired
    private ItemService itemService;
    @Autowired
    private AddressService addressService;*/

    // TODO 使用Feign改造
    @Autowired
    private LoadBalancerClient client;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVO create(PlaceOrderBO placeOrderBO) {
        OrderBO orderBO = placeOrderBO.getOrderBO();
        List<ShopcartBO> shopcartList = placeOrderBO.getShopcartList();

        // 0.订单信息
        String userId = orderBO.getUserId();
        Integer payMethod = orderBO.getPayMethod();
        String addressId = orderBO.getAddressId();
        String itemSpecIds = orderBO.getItemSpecIds();
        String leftMsg = orderBO.getLeftMsg();
        String orderId = sid.nextShort();
        Integer postAmount = 0; // 邮费

        // 1.创建订单
        Orders newOrder = new Orders();
        // 查询地址信息
        // UserAddress userAddress = addressService.queryById(addressId);
        // 服务间调用 TODO 使用Feign改造
        ServiceInstance choose = client.choose("FOODIE-USER-SERVICE");
        String target = String.format("http://%s:%s/address-api/address?addressId=%s",
                choose.getHost(),
                choose.getPort(),
                addressId);
        UserAddress userAddress = restTemplate.getForObject(target, UserAddress.class);

        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        newOrder.setPayMethod(payMethod); // 支付方式
        newOrder.setIsComment(YesOrNo.NO.type); // 是否留言
        newOrder.setIsDelete(YesOrNo.NO.type); // 是否逻辑删除
        newOrder.setLeftMsg(leftMsg); // 备注信息
        newOrder.setPostAmount(postAmount); // 邮费默认0

        newOrder.setReceiverAddress(userAddress.getProvince() + " " +
                userAddress.getCity()  + " " +
                userAddress.getDistrict()  + " " +
                userAddress.getDetail()); // 地址
        newOrder.setReceiverMobile(userAddress.getMobile()); // 电话
        newOrder.setReceiverName(userAddress.getReceiver()); // 姓名
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        Integer totalAmount = 0;
        Integer realPayAmount = 0;

        // 2.维护订单商品表并减少库存
        // 商品集合保存，用于清除购物车缓存
        List<ShopcartBO> needRemoveList = new ArrayList<>();
        // 规格信息循环
        String[] itemSpecs = itemSpecIds.split(",");
        for (String itemSpecId : itemSpecs) {
            // 查询规格信息
            //ItemsSpec itemsSpec = itemService.queryItemsSpec(itemSpecId);
            // 服务间调用 TODO 使用Feign改造
            ServiceInstance itemsSpecChoose = client.choose("FOODIE-ITEM-SERVICE");
            String itemsSpecTarget = String.format("http://%s:%s/item-api/itemSpec?specId=%s",
                    itemsSpecChoose.getHost(),
                    itemsSpecChoose.getPort(),
                    itemSpecId);
            ItemsSpec itemsSpec = restTemplate.getForObject(itemsSpecTarget, ItemsSpec.class);

            // 查询商品图片信息
            //ItemsImg itemsImg = itemService.queryItemsImg(itemsSpec.getItemId());
            // 服务间调用 TODO 使用Feign改造
            ServiceInstance itemsImgChoose = client.choose("FOODIE-ITEM-SERVICE");
            String itemsImgTarget = String.format("http://%s:%s/item-api/itemImage?itemId=%s",
                    itemsImgChoose.getHost(),
                    itemsImgChoose.getPort(),
                    itemsSpec.getItemId());
            ItemsImg itemsImg = restTemplate.getForObject(itemsImgTarget, ItemsImg.class);

            // 查询商品信息
            //Items items = itemService.queryItems(itemsSpec.getItemId());
            // 服务间调用 TODO 使用Feign改造
            ServiceInstance itemsChoose = client.choose("FOODIE-ITEM-SERVICE");
            String itemsTarget = String.format("http://%s:%s/item-api/item?itemId=%s",
                    itemsChoose.getHost(),
                    itemsChoose.getPort(),
                    itemsSpec.getItemId());
            Items items = restTemplate.getForObject(itemsTarget, Items.class);

            // 从Redis取购物车数据，获取商品数量
            ShopcartBO item = getBuyCountsFromCart(shopcartList,itemSpecId);;
            // 保存该数据，用于后续清楚购物车缓存
            needRemoveList.add(item);

            Integer buyCounts = Integer.valueOf(item.getBuyCounts());
            String orderItemId = sid.nextShort();
            OrderItems orderItem = new OrderItems();
            orderItem.setBuyCounts(buyCounts);
            orderItem.setId(orderItemId);
            orderItem.setOrderId(orderId);
            orderItem.setItemId(itemsSpec.getItemId());
            orderItem.setItemImg(itemsImg.getUrl());
            orderItem.setItemName(items.getItemName());
            orderItem.setItemSpecId(itemSpecId);
            orderItem.setItemSpecName(itemsSpec.getName());
            orderItem.setPrice(itemsSpec.getPriceDiscount());

            orderItemsMapper.insert(orderItem);

            // 计算总价
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 还需要减少库存
            //itemService.decreaseItemSpecStock(itemSpecId,buyCounts);
            // 服务间调用 TODO 使用Feign改造
            ServiceInstance decreaseStockChoose = client.choose("FOODIE-ITEM-SERVICE");
            String decreaseStock = String.format("http://%s:%s/item-api/decreaseStock?specId=%s&buyCounts=%s",
                    decreaseStockChoose.getHost(),
                    decreaseStockChoose.getPort(),
                    itemSpecId,
                    buyCounts);
            restTemplate.getForObject(decreaseStock,null);
        }

        newOrder.setTotalAmount(totalAmount); // 总价
        newOrder.setRealPayAmount(realPayAmount); // 优惠后价格

        // 插入订单表
        ordersMapper.insert(newOrder);

        // 3.维护订单状态表
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(orderStatus);

        // 4.创建支付中心订单信息
        MerchantOrderVO merchantOrderVO = new MerchantOrderVO();
        merchantOrderVO.setMerchantOrderId(orderId);
        merchantOrderVO.setMerchantUserId(userId);
        merchantOrderVO.setAmount(realPayAmount + postAmount);
        merchantOrderVO.setPayMethod(payMethod);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrderVO(merchantOrderVO);
        orderVO.setNeedRemoveList(needRemoveList);
        return orderVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String merchantOrderId, Integer type) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(merchantOrderId);
        orderStatus.setOrderStatus(type);
        orderStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatus getOrderStatus(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public void closeOrder() {
        // 获取所有未支付订单，判断当前时间-订单创建时间是否大于一天
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> waitPayOrders = orderStatusMapper.select(orderStatus);
        for (OrderStatus waitPayOrder : waitPayOrders) {
            //判断时间
            Date createdTime = waitPayOrder.getCreatedTime();
            int day = DateUtil.daysBetween(createdTime, new Date());
            if(day >= 1){
                // 关闭订单
                waitPayOrder.setOrderStatus(OrderStatusEnum.CLOSE.type);
                waitPayOrder.setCloseTime(new Date());
                orderStatusMapper.updateByPrimaryKeySelective(waitPayOrder);
            }
        }
    }

    /**
     * 从购物车中获取目标商品的购买数量
     * @param shopcartList
     * @param itemSpecId
     * @return
     */
    private ShopcartBO getBuyCountsFromCart(List<ShopcartBO> shopcartList, String itemSpecId) {
        for (ShopcartBO shopcartBO : shopcartList) {
            if(shopcartBO.getSpecId().equals(itemSpecId)){
                return shopcartBO;
            }
        }
        return null;
    }
}
