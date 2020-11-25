package com.pacee1.order.fallback.itemservice;

import com.google.common.collect.Lists;
import com.pacee1.item.pojo.vo.MyCommentVO;
import com.pacee1.pojo.PagedGridResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-11-25 17:44
 **/
@Component
/**
 * 这里必须添加请求路径，不然会和之前一样的问题，请求路径重复
 * 所以我们这里随便写了一个请求路径
 * 因为这个类是降级类，不会发起远程调用，写什么都无所谓
 */
@RequestMapping("itemCommentsFallback")
public class ItemCommentsFallback implements ItemCommentsFeignClient {

    // 降级逻辑
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
}
