package com.pacee1.order.fallback.itemservice;

import com.google.common.collect.Lists;
import com.pacee1.item.pojo.vo.MyCommentVO;
import com.pacee1.pojo.PagedGridResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-11-25 17:48
 **/
@Component
public class ItemCommentsFallbackFactory implements FallbackFactory<ItemCommentsFeignClient> {
    @Override
    public ItemCommentsFeignClient create(Throwable cause) {
        return new ItemCommentsFeignClient() {
            @Override
            public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
                MyCommentVO commentVO = new MyCommentVO();
                commentVO.setContent("正在加载中");

                PagedGridResult result = new PagedGridResult();
                result.setRows(Lists.newArrayList(commentVO));
                result.setTotal(1);
                result.setRecords(1);
                return result;
            }

            @Override
            public void saveComments(Map<String, Object> map) {

            }
        };
    }
}
