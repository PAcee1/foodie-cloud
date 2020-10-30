package com.pacee1.cart.service;

import com.pacee1.pojo.ShopcartBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-10-22 15:33
 **/
@FeignClient("foodie-cart-service")
@RequestMapping("cart-api")
public interface CartService {

    @PostMapping("/cart")
    void add(@RequestParam("userId") String userId,
                       @RequestBody ShopcartBO shopcartBO);

    @DeleteMapping("/cart")
    void del(@RequestParam("userId") String userId,
                       @RequestParam("itemSpecId") String itemSpecId);

    @DeleteMapping("/clearCart")
    void clear(@RequestParam("userId") String userId,
            @RequestParam("jsonShop") String jsonShop);
}
