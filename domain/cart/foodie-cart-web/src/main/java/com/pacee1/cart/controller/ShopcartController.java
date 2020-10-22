package com.pacee1.cart.controller;

import com.pacee1.cart.service.CartService;
import com.pacee1.pojo.ShopcartBO;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.RedisOperator;
import com.pacee1.pojo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type IndexController.java
 * @Desc
 * @date 2020/6/8 15:20
 */
@RestController
@RequestMapping("shopcart")
@Api(value = "购物车相关接口",tags = "购物车相关接口")
public class ShopcartController {

    /**   购物车使用Cookie+Redis实现   **/

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @ApiOperation(value = "添加购物车接口",notes = "添加购物车接口")
    public ResponseResult add(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO){
        if(userId == null){
            return ResponseResult.errorMsg("用户不存在");
        }

        cartService.add(userId,shopcartBO);

        return ResponseResult.ok();
    }


    @PostMapping("/del")
    @ApiOperation(value = "删除购物车商品接口",notes = "删除购物车商品接口")
    public ResponseResult del(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestParam String itemSpecId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return ResponseResult.errorMsg("参数为空");
        }

        cartService.del(userId, itemSpecId);

        return ResponseResult.ok();
    }
}
