package com.pacee1.search.mapper;

import com.pacee1.search.pojo.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SearchMapper {

    /**
     * 根据分类搜索商品
     * @param catId
     * @param sort
     * @return
     */
    List<SearchItemsVO> searchItemsByCat(@Param("catId") Integer catId, @Param("sort") String sort);

    /**
     * 搜索商品
     * @param keyword
     * @param sort
     * @return
     */
    List<SearchItemsVO> searchItems(@Param("keyword")String keyword, @Param("sort") String sort);

}