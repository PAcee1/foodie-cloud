package com.pacee1.order.pojo.vo;

import com.pacee1.pojo.ShopcartBO;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/12 15:13
 * @Classname OrderVO
 *
 * 保存订单id和支付中心订单信息
 */
public class OrderVO {

    private String orderId;
    private MerchantOrderVO merchantOrderVO;
    // 保存Redis需要清除的商品集合
    private List<ShopcartBO> needRemoveList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrderVO getMerchantOrderVO() {
        return merchantOrderVO;
    }

    public void setMerchantOrderVO(MerchantOrderVO merchantOrderVO) {
        this.merchantOrderVO = merchantOrderVO;
    }

    public List<ShopcartBO> getNeedRemoveList() {
        return needRemoveList;
    }

    public void setNeedRemoveList(List<ShopcartBO> needRemoveList) {
        this.needRemoveList = needRemoveList;
    }
}
