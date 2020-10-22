package com.pacee1.search.pojo.vo;

/**
 * @Created by pace
 * @Date 2020/6/9 16:29
 * @Classname ItemCommentVO
 * 商品搜索使用的VO
 */
public class SearchItemsVO {

    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;
    private Integer price; // 全部用分表示，所以用int接收

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(Integer sellCounts) {
        this.sellCounts = sellCounts;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
