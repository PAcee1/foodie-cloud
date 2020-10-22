package com.pacee1.search.service;

import com.pacee1.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.*;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-10-22 15:33
 **/
@RestController("search-api")
public interface SearchService {

    /**
     * 分页搜索商品
     * @param keyword
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/searchItems")
    PagedGridResult searchItems(@RequestParam("keyword") String keyword,
                                @RequestParam(value = "sort",required = false) String sort,
                                @RequestParam(value = "page",required = false) Integer page,
                                @RequestParam(value = "pageSize",required = false) Integer pageSize);

    // 通过ES搜索
    @GetMapping("/searchItemsES")
    PagedGridResult searchItemsES(@RequestParam("keyword") String keyword,
                                @RequestParam(value = "sort",required = false) String sort,
                                @RequestParam(value = "page",required = false) Integer page,
                                @RequestParam(value = "pageSize",required = false) Integer pageSize);

    /**
     * 分页搜索商品根据分类
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/searchCatItems")
    PagedGridResult searchItemsByCat(@RequestParam("catId") Integer catId,
                                     @RequestParam(value = "sort",required = false) String sort,
                                     @RequestParam(value = "page",required = false) Integer page,
                                     @RequestParam(value = "pageSize",required = false) Integer pageSize);
}
