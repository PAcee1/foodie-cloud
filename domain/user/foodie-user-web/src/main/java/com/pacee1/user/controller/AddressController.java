package com.pacee1.user.controller;

import com.pacee1.user.pojo.UserAddress;
import com.pacee1.user.pojo.bo.UserAddressBO;
import com.pacee1.user.service.AddressService;
import com.pacee1.utils.MobileEmailUtils;
import com.pacee1.pojo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type IndexController.java
 * @Desc
 * @date 2020/6/8 15:20
 */
@RestController
@RequestMapping("address")
@Api(value = "用户地址接口",tags = "用户地址相关接口")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/list")
    @ApiOperation(value = "查询用户地址",notes = "查询用户地址接口")
    public ResponseResult list(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){
        if(userId == null){
            return ResponseResult.errorMsg("用户不存在");
        }

        List<UserAddress> userAddresses = addressService.queryUserAddress(userId);

        return ResponseResult.ok(userAddresses);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增用户地址",notes = "新增用户地址接口")
    public ResponseResult add(
            @ApiParam(name = "addressBO",value = "地址信息",required = true)
            @RequestBody UserAddressBO addressBO){
        ResponseResult checkAddress = checkAddress(addressBO);
        if(checkAddress.getStatus() != 200){
            return checkAddress;
        }

        addressService.add(addressBO);

        return ResponseResult.ok();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改用户地址",notes = "修改用户地址接口")
    public ResponseResult update(
            @ApiParam(name = "addressBO",value = "地址信息",required = true)
            @RequestBody UserAddressBO addressBO){
        if(StringUtils.isBlank(addressBO.getAddressId())){
            return ResponseResult.errorMsg("地址id为空");
        }

        ResponseResult checkAddress = checkAddress(addressBO);
        if(checkAddress.getStatus() != 200){
            return checkAddress;
        }

        addressService.edit(addressBO);

        return ResponseResult.ok();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除用户地址",notes = "删除用户地址接口")
    public ResponseResult delete(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return ResponseResult.errorMsg("");
        }

        addressService.delete(userId,addressId);

        return ResponseResult.ok();
    }

    @PostMapping("/setDefault")
    @ApiOperation(value = "设置默认地址",notes = "设置默认地址接口")
    public ResponseResult setDefault(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return ResponseResult.errorMsg("");
        }

        addressService.setDefaultAddress(userId,addressId);

        return ResponseResult.ok();
    }

    /**
     * 校验地址
     * @param addressBO
     * @return
     */
    private ResponseResult checkAddress(UserAddressBO addressBO){
        if(StringUtils.isBlank(addressBO.getReceiver())){
            return ResponseResult.errorMsg("收货人不能为空");
        }
        if(StringUtils.isBlank(addressBO.getMobile())){
            return ResponseResult.errorMsg("手机号不能为空");
        }
        if(!MobileEmailUtils.checkMobileIsOk(addressBO.getMobile())){
            return ResponseResult.errorMsg("手机号格式不正确");
        }

        if(StringUtils.isBlank(addressBO.getCity())||
        StringUtils.isBlank(addressBO.getProvince()) ||
        StringUtils.isBlank(addressBO.getDistrict()) ||
        StringUtils.isBlank(addressBO.getDetail())){
            return ResponseResult.errorMsg("收货地址不能为空");
        }
        return ResponseResult.ok();
    }

}
