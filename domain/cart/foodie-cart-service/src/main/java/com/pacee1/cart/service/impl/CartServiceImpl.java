package com.pacee1.cart.service.impl;

import com.pacee1.cart.service.CartService;
import com.pacee1.pojo.ResponseResult;
import com.pacee1.pojo.ShopcartBO;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-10-22 15:36
 **/
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public void add(String userId, ShopcartBO shopcartBO) {
        //System.out.println(shopcartBO);
        // 添加商品到Redis购物车中
        String shopcatStr = redisOperator.get("shopcart:" + userId);
        List<ShopcartBO> shopcartList = null;
        // 判断缓存是否存在
        if(!StringUtils.isBlank(shopcatStr)){
            shopcartList = JsonUtils.jsonToList(shopcatStr, ShopcartBO.class);
            // 如果存在，判断当前添加商品是否已经存在，存在则数量添加，不存在则直接添加
            boolean isHaving = false;
            for (ShopcartBO bo : shopcartList) {
                if(bo.getSpecId().equals(shopcartBO.getSpecId())){
                    // 数量添加
                    bo.setBuyCounts(bo.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if(!isHaving){
                shopcartList.add(shopcartBO);
            }
        }else {
            // 不存在，新建购物车
            shopcartList = new ArrayList<>();
            // 直接添加到购物车
            shopcartList.add(shopcartBO);
        }

        // 重新存放到Redis
        redisOperator.set("shopcart:"+userId , JsonUtils.objectToJson(shopcartList));
    }

    @Override
    public void del(String userId, String itemSpecId) {
        // 从购物车删除商品
        String shopcatStr = redisOperator.get("shopcart:" + userId);
        // 判断缓存是否存在
        if(!StringUtils.isBlank(shopcatStr)){
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcatStr, ShopcartBO.class);
            for (ShopcartBO bo : shopcartList) {
                if(bo.getSpecId().equals(itemSpecId)){
                    // 从list中删除
                    shopcartList.remove(bo);
                    break;
                }
            }
            // 更新Redis
            redisOperator.set("shopcart:"+userId , JsonUtils.objectToJson(shopcartList));
        }
    }
}
