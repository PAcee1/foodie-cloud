package com.pacee1.item.mapper;

import com.pacee1.item.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom{

    void saveCommentList(Map<String, Object> paramMap);

    List<MyCommentVO> queryCommentList(@Param("userId") String userId);
}