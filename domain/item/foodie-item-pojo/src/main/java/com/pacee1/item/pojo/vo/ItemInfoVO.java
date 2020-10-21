package com.pacee1.item.pojo.vo;

import com.pacee1.item.pojo.Items;
import com.pacee1.item.pojo.ItemsImg;
import com.pacee1.item.pojo.ItemsParam;
import com.pacee1.item.pojo.ItemsSpec;

import java.util.List;

/**
 * @Created by pace
 * @Date 2020/6/8 17:34
 * @Classname ItemInfoVO
 * 保存商品相关VO
 */
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }
}
