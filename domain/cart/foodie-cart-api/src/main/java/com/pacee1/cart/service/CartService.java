package com.pacee1.cart.service;

import com.pacee1.pojo.ResponseResult;
import com.pacee1.pojo.ShopcartBO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-10-22 15:33
 **/
@RestController("cart-api")
public interface CartService {

    @PostMapping("/cart")
    void add(@RequestParam("userId") String userId,
                       @RequestBody ShopcartBO shopcartBO);

    @DeleteMapping("/cart")
    void del(@RequestParam("userId") String userId,
                       @RequestParam("itemSpecId") String itemSpecId);
}
