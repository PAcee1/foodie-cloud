package com.pacee1.search.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pacee1.pojo.PagedGridResult;
import com.pacee1.search.mapper.SearchMapper;
import com.pacee1.search.pojo.bo.ItemsDoc;
import com.pacee1.search.pojo.vo.SearchItemsVO;
import com.pacee1.search.service.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-10-22 16:16
 **/
@RestController
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchMapper searchItems;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(String keyword, String sort, Integer page, Integer pageSize) {
        // 分页
        PageHelper.startPage(page,pageSize);

        List<SearchItemsVO> list = searchItems.searchItems(keyword, sort);

        return setPagedGridResult(list,page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItemsByCat(Integer catId, String sort, Integer page, Integer pageSize) {
        // 分页
        PageHelper.startPage(page,pageSize);

        List<SearchItemsVO> list = searchItems.searchItemsByCat(catId, sort);

        return setPagedGridResult(list,page);
    }

    @Override
    public PagedGridResult searchItemsES(String keyword, String sort, Integer page, Integer size) {
        // 分页条件
        Pageable pageable = PageRequest.of(page, size);
        // 高亮
        HighlightBuilder.Field name = new HighlightBuilder.Field("itemName");
        //.preTags("<font style='color:red'>")
        //.postTags("</font>")
        //.fragmentSize(100);
        // 排序
        SortBuilder sortBuilder = null;
        if(sort.equals("c")){
            sortBuilder = new FieldSortBuilder("sellCounts")
                    .order(SortOrder.DESC);
        }else if(sort.equals("p")){
            sortBuilder = new FieldSortBuilder("price")
                    .order(SortOrder.ASC);
        }else {
            sortBuilder = new FieldSortBuilder("itemName.keyword")
                    .order(SortOrder.ASC);
        }

        // 搜索查询条件
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("itemName",keyword))
                .withHighlightFields(name) // 高亮
                .withPageable(pageable) // 分页
                .withSort(sortBuilder) // 排序
                .build();

        // 搜索
        AggregatedPage<ItemsDoc> items = esTemplate.queryForPage(searchQuery, ItemsDoc.class,new SearchResultMapper(){

            // 组装结果数据
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<T> result = new ArrayList<>();
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    Map<String, HighlightField> hmap = hit.getHighlightFields();
                    Map<String, Object> smap = hit.getSourceAsMap();
                    result.add((T)createEsDoc(smap,hmap));
                }

                AggregatedPage<T> aggregatedPage = new AggregatedPageImpl<T>(result,pageable,
                        searchResponse.getHits().getTotalHits());

                return aggregatedPage;
            }
        });

        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setRows(items.getContent());
        pagedGridResult.setPage(page + 1);
        pagedGridResult.setTotal(items.getTotalPages());
        pagedGridResult.setRecords(items.getTotalElements());

        return pagedGridResult;
    }

    private ItemsDoc createEsDoc(Map<String, Object> smap,Map<String, HighlightField> hmap){
        ItemsDoc item = new ItemsDoc();
        if (smap.get("itemId") != null)
            item.setItemId(smap.get("itemId").toString());
        if (hmap.get("itemName") != null)
            item.setItemName(hmap.get("itemName").fragments()[0].toString());
        if(smap.get("imgUrl") != null)
            item.setImgUrl(smap.get("imgUrl").toString());
        if(smap.get("price") != null)
            item.setPrice(Integer.parseInt(smap.get("price").toString()));
        if(smap.get("sellCounts") != null)
            item.setSellCounts(Integer.parseInt(smap.get("sellCounts").toString()));
        return item;
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
