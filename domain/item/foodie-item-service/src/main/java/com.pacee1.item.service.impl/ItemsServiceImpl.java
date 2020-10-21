package com.pacee1.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pacee1.enums.CommentLevel;
import com.pacee1.enums.YesOrNo;
import com.pacee1.item.mapper.*;
import com.pacee1.item.pojo.*;
import com.pacee1.item.pojo.vo.CommentLevelCountsVO;
import com.pacee1.item.pojo.vo.ItemCommentVO;
import com.pacee1.item.pojo.vo.ShopcartVO;
import com.pacee1.item.service.ItemService;
import com.pacee1.utils.DesensitizationUtil;
import com.pacee1.pojo.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 10:06
 * @Classname CarouselServiceImpl
 */
// 改造成Eureka后，基于Http，所以需要用RestController
@RestController
@Slf4j
public class ItemsServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItems(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemsImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemsSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemsParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCountsByLevel(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCountsByLevel(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCountsByLevel(itemId, CommentLevel.BAD.type);
        // 总平均数
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO result = new CommentLevelCountsVO();
        result.setGoodCounts(goodCounts);
        result.setNormalCounts(normalCounts);
        result.setBadCounts(badCounts);
        result.setTotalCounts(totalCounts);

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryItemComments(String itemId, Integer level, Integer page, Integer size) {
        // 分页
        PageHelper.startPage(page,size);

        List<ItemCommentVO> commentVOS = itemsMapperCustom.queryItemComments(itemId, level);
        // 对用户名脱敏
        for (ItemCommentVO commentVO : commentVOS) {
            commentVO.setNickname(DesensitizationUtil.commonDisplay(commentVO.getNickname()));
        }

        return setPagedGridResult(commentVOS,page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        // 分隔ids，组装成集合
        String[] strings = specIds.split(",");
        List list = new ArrayList();
        Collections.addAll(list,strings);

        List<ShopcartVO> shopcartVOS = itemsMapperCustom.queryItemsBySpecIds(list);
        return shopcartVOS;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec queryItemsSpec(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsImg queryItemsImg(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setIsMain(YesOrNo.YES.type);
        itemsImg.setItemId(itemId);
        return itemsImgMapper.selectOne(itemsImg);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseItemSpecStock(String specId, Integer buyCounts) {
        /**
         * 减少库存很关键，因为高并发场景下处理不当，就会出现超卖问题
         * 1.使用synchronized 集群下无用
         * 2.锁数据库，性能低下
         * 3.分布式锁，zookeeper或redis
         */
        // TODO 需优化，当前使用乐观锁简单实现
        Integer result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if(result != 1){
            throw new RuntimeException("订单创建失败，库存不足");
        }
    }

    /**
     * 获取各个等级评价数
     * @param itemId
     * @param level
     * @return
     */
    private Integer getCommentCountsByLevel(String itemId,Integer level){
        ItemsComments itemsComments = new ItemsComments();
        itemsComments.setItemId(itemId);
        itemsComments.setCommentLevel(level);

        return itemsCommentsMapper.selectCount(itemsComments);
    }

    /**
     * 封装分页结果
     * @param list
     * @param page
     * @return
     */
    private PagedGridResult setPagedGridResult(List<?> list,Integer page){
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult result = new PagedGridResult();
        result.setPage(page);
        result.setRows(list);
        result.setRecords(pageInfo.getTotal());
        result.setTotal(pageInfo.getPages());

        return result;
    }
}
