package com.pacee1.user.controller.center;

import com.pacee1.user.pojo.Users;
import com.pacee1.user.service.center.CenterUserService;
import com.pacee1.pojo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Created by pace
 * @Date 2020/6/15 15:36
 * @Classname CenterController
 */
@RestController
@RequestMapping("center")
@Api(value = "用户中心相关接口",tags = "用户中心相关接口")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @PostMapping("/userInfo")
    @ApiOperation(value = "获取用户信息",notes = "获取用户信息接口")
    public ResponseResult userInfo(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){
        if(userId == null){
            return ResponseResult.errorMsg("用户不存在");
        }

        Users users = centerUserService.queryUserInfo(userId);

        return ResponseResult.ok(users);
    }
}
