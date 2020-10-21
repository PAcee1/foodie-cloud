package com.pacee1.item.service;

import com.pacee1.item.pojo.Items;
import com.pacee1.item.pojo.ItemsImg;
import com.pacee1.item.pojo.ItemsParam;
import com.pacee1.item.pojo.ItemsSpec;
import com.pacee1.item.pojo.vo.CommentLevelCountsVO;
import com.pacee1.item.pojo.vo.ShopcartVO;
import com.pacee1.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 17:20
 * @Classname ItemService
 */
// 接口层要对外提供服务，当使用Feign组件时，调用起来更加方便
@RequestMapping("item-api")
public interface ItemService {

    /**
     * 查询商品基本信息
     * @param itemId
     * @return
     */
    @GetMapping("item")
    Items queryItems(@RequestParam("itemId") String itemId);

    /**
     * 查询商品图片列表
     * @param itemId
     * @return
     */
    @GetMapping("itemImages")
    List<ItemsImg> queryItemsImgList(@RequestParam("itemId")String itemId);

    /**
     * 查询商品规格信息
     * @param itemId
     * @return
     */
    @GetMapping("itemSpecs")
    List<ItemsSpec> queryItemsSpecList(@RequestParam("itemId")String itemId);

    /**
     * 查询商品参数信息
     * @param itemId
     * @return
     */
    @GetMapping("itemParam")
    ItemsParam queryItemsParam(@RequestParam("itemId")String itemId);

    /**
     * 查询评价数量
     * @param itemId
     * @return
     */
    @GetMapping("countComments")
    CommentLevelCountsVO queryCommentCounts(@RequestParam("itemId")String itemId);

    /**
     * 分页查询评价
     * @param itemId
     * @param level
     * @param page
     * @param size
     * @return
     */
    @GetMapping("pagedComments")
    PagedGridResult queryItemComments(@RequestParam("itemId")String itemId,
                                      @RequestParam(value = "level",required = false)Integer level,
                                      @RequestParam(value = "size",required = false)Integer page, Integer size);

   /* *//**
     * 分页搜索商品
     * @param keyword
     * @param sort
     * @param page
     * @param size
     * @return
     *//*
    PagedGridResult searchItems(String keyword, String sort, Integer page, Integer size);

    *//**
     * 分页搜索商品根据分类
     * @param catId
     * @param sort
     * @param page
     * @param size
     * @return
     *//*
    PagedGridResult searchItemsByCat(Integer catId, String sort, Integer page, Integer size);*/

    /**
     * 根据商品规格id查询商品
     * @param specIds
     * @return
     */
    @GetMapping("getCartBySpecIds")
    List<ShopcartVO> queryItemsBySpecIds(@RequestParam("specIds") String specIds);

    /**
     * 根据规格Id查询规格，单条数据
     * @param specId
     * @return
     */
    @GetMapping("itemSpec")
    ItemsSpec queryItemsSpec(@RequestParam("specId")String specId);

    /**
     * 根据商品id查询图片信息
     * @param itemId
     * @return
     */
    @GetMapping("itemImage")
    ItemsImg queryItemsImg(@RequestParam("itemId")String itemId);

    @PostMapping("decreaseStock")
    void decreaseItemSpecStock(@RequestParam("specId") String specId,
                               @RequestParam("buyCounts")Integer buyCounts);
}
