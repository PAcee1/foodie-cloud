package com.pacee1.user.service.impl;

import com.pacee1.enums.YesOrNo;
import com.pacee1.user.mapper.UserAddressMapper;
import com.pacee1.user.pojo.UserAddress;
import com.pacee1.user.pojo.bo.UserAddressBO;
import com.pacee1.user.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author pace
 * @version v1.0
 * @Type UserServiceImpl.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@RestController
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserAddress> queryUserAddress(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        List<UserAddress> result = userAddressMapper.select(userAddress);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(UserAddressBO userAddressBO) {
        UserAddress userAddress = new UserAddress();
        // 设置成正常的实体类
        BeanUtils.copyProperties(userAddressBO,userAddress);
        // 判断是否有地址，如果没有设置为默认地址
        List<UserAddress> userAddresses = queryUserAddress(userAddress.getUserId());
        if(userAddresses == null && userAddresses.size() < 1 ){
            userAddress.setIsDefault(YesOrNo.YES.type);
        }

        // 设置其他属性
        userAddress.setId(sid.nextShort());
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void edit(UserAddressBO userAddressBO) {
        UserAddress userAddress = new UserAddress();
        // 设置成正常的实体类
        BeanUtils.copyProperties(userAddressBO,userAddress);

        // 设置
        userAddress.setId(userAddressBO.getAddressId()); // 变量名称不同需手动转换
        userAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);

        userAddressMapper.delete(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void setDefaultAddress(String userId, String addressId) {
        // 首先查找当前默认地址，修改
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> userAddresses = userAddressMapper.select(userAddress);
        for (UserAddress address : userAddresses) {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }

        // 然后再修改新的默认地址
        UserAddress newUserAddress = new UserAddress();
        newUserAddress.setId(addressId);
        newUserAddress.setUserId(userId);
        newUserAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(newUserAddress);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserAddress queryById(String addressId) {
        return userAddressMapper.selectByPrimaryKey(addressId);
    }
}
