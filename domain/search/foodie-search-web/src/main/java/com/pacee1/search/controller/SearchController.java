package com.pacee1.search.controller;

import com.pacee1.search.service.SearchService;
import com.pacee1.pojo.PagedGridResult;
import com.pacee1.pojo.ResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-08-12 15:20
 **/
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/old/items")
    @ApiOperation(value = "基于数据库搜索商品",notes = "基于数据库搜索商品接口")
    public ResponseResult search(
            @ApiParam(name = "keywords",value = "关键字",required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort",value = "排序类型，c为销量，p为价格，k为名称",required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "当前页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(keywords == null){
            return ResponseResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }

        PagedGridResult result = searchService.searchItems(keywords, sort, page, pageSize);

        return ResponseResult.ok(result);
    }

    @GetMapping("/catItems")
    @ApiOperation(value = "搜索商品通过分类",notes = "搜索商品通过分类接口")
    public ResponseResult catItems(
            @ApiParam(name = "catId",value = "分类ID",required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort",value = "排序类型，c为销量，p为价格，k为名称",required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "当前页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(catId == null){
            return ResponseResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }

        PagedGridResult result = searchService.searchItemsByCat(catId, sort, page, pageSize);

        return ResponseResult.ok(result);
    }

    @GetMapping("/items")
    @ApiOperation(value = "基于ES搜索商品",notes = "基于ES搜索商品接口")
    public ResponseResult searchES(
            @ApiParam(name = "keywords",value = "关键字",required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort",value = "排序类型，c为销量，p为价格，k为名称",required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "当前页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页数量",required = false)
            @RequestParam Integer pageSize){
        if(keywords == null){
            return ResponseResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        page --;

        if(pageSize == null){
            pageSize = 20;
        }

        PagedGridResult result = searchService.searchItemsES(keywords, sort, page, pageSize);

        return ResponseResult.ok(result);
    }
}
