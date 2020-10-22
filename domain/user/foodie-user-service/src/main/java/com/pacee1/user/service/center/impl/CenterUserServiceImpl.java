package com.pacee1.user.service.center.impl;

import com.pacee1.user.mapper.UsersMapper;
import com.pacee1.user.pojo.Users;
import com.pacee1.user.pojo.bo.center.CenterUserBO;
import com.pacee1.user.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author pace
 * @version v1.0
 * @Type UserServiceImpl.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@RestController
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserInfo(String userId) {
        return usersMapper.selectByPrimaryKey(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users updateUserInfo(String userId, CenterUserBO userBO) {
        Users users = new Users();
        BeanUtils.copyProperties(userBO,users);
        users.setUpdatedTime(new Date());
        users.setId(userId);
        usersMapper.updateByPrimaryKeySelective(users);
        return users;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users updateUserFace(String userId, String faceUrl) {
        Users users = new Users();
        users.setUpdatedTime(new Date());
        users.setFace(faceUrl);
        users.setId(userId);
        usersMapper.updateByPrimaryKeySelective(users);
        return users;
    }
}
