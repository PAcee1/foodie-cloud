package com.pacee1.user.pojo.bo;

import io.swagger.annotations.ApiModel;

/**
 * @author pace
 * @version v1.0
 * @Type UserBO.java
 * @Desc
 * @date 2020/5/17 16:43
 */
@ApiModel(value = "用户地址BO",description = "用户增删改地址BO")
public class UserAddressBO {

    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String detail;
    private String province;
    private String city;
    private String district;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
