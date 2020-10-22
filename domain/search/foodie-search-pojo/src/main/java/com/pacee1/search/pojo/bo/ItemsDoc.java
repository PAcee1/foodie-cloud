package com.pacee1.search.pojo.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * <p>商品信息</p>
 *
 * @author : Pace
 * @date : 2020-08-12 17:00
 **/
@Document(indexName = "foodie-items",type = "doc")
public class ItemsDoc {

    @Id
    @Field(store = true,type = FieldType.Text,index = false)
    private String itemId;

    @Field(store = true,type = FieldType.Text,index = true)
    private String itemName;

    @Field(store = true,type = FieldType.Text,index = false)
    private String imgUrl;

    @Field(store = true,type = FieldType.Integer)
    private Integer price;

    @Field(store = true,type = FieldType.Integer)
    private Integer sellCounts;

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

    public Integer getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(Integer sellCounts) {
        this.sellCounts = sellCounts;
    }

    @Override
    public String toString() {
        return "Items{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", price=" + price +
                ", sellCounts=" + sellCounts +
                '}';
    }
}
