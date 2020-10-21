package com.pacee1.item.controller;

import com.pacee1.item.pojo.Items;
import com.pacee1.item.pojo.ItemsImg;
import com.pacee1.item.pojo.ItemsParam;
import com.pacee1.item.pojo.ItemsSpec;
import com.pacee1.pojo.PagedGridResult;
import com.pacee1.pojo.ResponseResult;
import com.pacee1.item.pojo.vo.*;
import com.pacee1.item.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type IndexController.java
 * @Desc
 * @date 2020/6/8 15:20
 */
@RestController
@RequestMapping("items")
@Api(value = "商品相关接口",tags = "商品相关接口")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/info/{itemId}")
    @ApiOperation(value = "根据id查询商品相关信息",notes = "根据id查询商品相关信息")
    public ResponseResult info(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @PathVariable String itemId){
        if(itemId == null){
            return ResponseResult.errorMsg("商品ID不存在");
        }
        Items items = itemService.queryItems(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemsImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemsSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemsParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);

        return ResponseResult.ok(itemInfoVO);
    }

    @GetMapping("/commentLevel")
    @ApiOperation(value = "查询商品评价数量",notes = "查询商品评价数量")
    public ResponseResult commentLevel(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @RequestParam String itemId){
        if(itemId == null){
            return ResponseResult.errorMsg("商品ID不存在");
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return ResponseResult.ok(countsVO);
    }

    @GetMapping("/comments")
    @ApiOperation(value = "查询商品评价",notes = "查询商品评价")
    public ResponseResult comments(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level",value = "评价类型",required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page",value = "当前页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(itemId == null){
            return ResponseResult.errorMsg("商品ID不存在");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }

        PagedGridResult result = itemService.queryItemComments(itemId, level, page, pageSize);

        return ResponseResult.ok(result);
    }

    @GetMapping("/refresh")
    @ApiOperation(value = "购物车页面，根据商品规格id刷新最新商品信息",notes = "购物车页面，根据商品规格id刷新最新商品信息")
    public ResponseResult refresh(
            @ApiParam(name = "itemSpecIds",value = "商品规格ids",required = true)
            @RequestParam String itemSpecIds){
        if(itemSpecIds == null){
            return ResponseResult.errorMsg(null);
        }

        List<ShopcartVO> result = itemService.queryItemsBySpecIds(itemSpecIds);
        return ResponseResult.ok(result);
    }
}
