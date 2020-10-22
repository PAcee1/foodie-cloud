package com.pacee1.user.controller;

import com.pacee1.pojo.ShopcartBO;
import com.pacee1.user.pojo.Users;
import com.pacee1.user.pojo.bo.UserBO;
import com.pacee1.user.pojo.vo.UsersVO;
import com.pacee1.user.service.UserService;
import com.pacee1.utils.CookieUtils;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.RedisOperator;
import com.pacee1.pojo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author pace
 * @version v1.0
 * @Type PassportController.java
 * @Desc
 * @date 2020/5/17 15:20
 */
@RestController
@RequestMapping("passport")
@Api(value = "登录注册接口",tags = "用户登录注册的接口")
public class PassportController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/usernameIsExist")
    @ApiOperation(value = "用户名校验是否存在",notes = "用户名校验是否存在")
    public ResponseResult usernameIsExist(@RequestParam String username){
        if(StringUtils.isBlank(username)){
            return ResponseResult.errorMsg("用户名不能为空");
        }

        boolean exist = userService.queryUsernameIsExist(username);
        if(exist){
            return ResponseResult.errorMsg("用户名已存在");
        }

        return ResponseResult.ok();
    }

    @PostMapping("/regist")
    @ApiOperation(value = "用户注册",notes = "用户注册")
    public ResponseResult regist(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        // 1.校验
        if(StringUtils.isBlank(username) ||
        StringUtils.isBlank(password) ||
        StringUtils.isBlank(confirmPassword)){
            return ResponseResult.errorMsg("用户名或密码不能为空");
        }

        boolean exist = userService.queryUsernameIsExist(username);
        if(exist){
            return ResponseResult.errorMsg("用户名已存在");
        }

        if(password.length() < 6){
            return ResponseResult.errorMsg("密码不能小于6位");
        }

        if(!StringUtils.equals(password,confirmPassword)){
            return ResponseResult.errorMsg("两次密码不一致");
        }

        // 2.保存
        Users user = userService.createUser(userBO);
        // 首先清空用户敏感信息
        user = setUserNull(user);

        // 3.保存Session到Redis
        // 创建Token，使用UUID
        String userToken = UUID.randomUUID().toString().trim();
        redisOperator.set("redis_user_token:" + user.getId(),userToken);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        usersVO.setUserUniqueToken(userToken);

        // 4.设置cookie
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(usersVO),true);

        // 同步购物车数据
        SyncShopcart(user.getId(),request,response);

        return ResponseResult.ok();
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录",notes = "用户登录")
    public ResponseResult login(@RequestBody UserBO userBO,
                                HttpServletRequest request,
                                HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 1.校验
        if(StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)){
            return ResponseResult.errorMsg("用户名或密码不能为空");
        }

        // 2.登录
        Users user = userService.queryUserForLogin(username,password);
        if(user == null){
            return  ResponseResult.errorMsg("用户名或密码错误");
        }
        // 首先清空用户敏感信息
        user = setUserNull(user);

        // 创建Token，使用UUID
        String userToken = UUID.randomUUID().toString().trim();
        redisOperator.set("redis_user_token:" + user.getId(),userToken);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        usersVO.setUserUniqueToken(userToken);

        // 4.设置cookie
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(usersVO),true);

        // 同步购物车数据
        SyncShopcart(user.getId(),request,response);

        return ResponseResult.ok(user);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出登录",notes = "用户退出登录")
    public ResponseResult logout(@RequestParam String userId,
                                HttpServletRequest request,
                                HttpServletResponse response){

        // 1.清除cookie
        CookieUtils.deleteCookie(request,response,"user");

        // 清除分布式会话
        redisOperator.del("redis_user_token:" + userId);

        // 清除cookie购物车
        CookieUtils.deleteCookie(request,response,"shopcart");

        return ResponseResult.ok();
    }

    /**
     * 同步cookie和redis购物车
     * @param userId
     */
    // TODO 放到购物车模块
    private void SyncShopcart(String userId,
                              HttpServletRequest request,
                              HttpServletResponse response){
        /**
         * 同步逻辑：
         * 1.redis没有购物车，cookie有：将cookie的直接放到redis中
         *                  cookie没有：不做处理
         * 2.redis有购物车，cookie有：
         *                      商品在redis，不在cookie，同步到cookie
         *                      商品在cookie，不在redis，同步到redis
         *                      商品都存在，以cookie的数量为准（参考京东）
         *                cookie没有：将redis商品数据同步到cookie中
         */
        // 获取redis
        String redisShopcart = redisOperator.get("shopcart:" + userId);
        
        // 获取cookie
        String cookieShopcart = CookieUtils.getCookieValue(request, "shopcart", true);

        if(StringUtils.isBlank(redisShopcart)){
            if(!StringUtils.isBlank(cookieShopcart)){
                // redis不存在，cookie存在，同步到redis
                redisOperator.set("shopcart:" + userId,cookieShopcart);
            }
        }else {
            if(StringUtils.isBlank(cookieShopcart)){
                // redis存在，cookie不存在，同步到cookie
                CookieUtils.setCookie(request,response,"shopcart", redisShopcart,true);
            }else {
                // 都存在，进行合并
                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(redisShopcart, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(cookieShopcart, ShopcartBO.class);
                
                // 存放cookie需要删除的商品
                List<ShopcartBO> needRemoveList = new ArrayList<>();
                
                // 循环寻找相同商品
                for (ShopcartBO scRedis : shopcartListRedis) {
                    String specIdRedis = scRedis.getSpecId();
                    for (ShopcartBO scCookie : shopcartListCookie) {
                        String specIdCookie = scCookie.getSpecId();
                        
                        if(specIdRedis.equals(specIdRedis)){
                            // 将Cookie商品数量覆盖Redis商品的数量
                            scRedis.setBuyCounts(scCookie.getBuyCounts());
                            // 将cookie相同商品添加到待删除商品集合 , 方便后期合并
                            needRemoveList.add(scCookie);
                        }
                    }
                }
                
                // Cookie删除商品
                shopcartListCookie.removeAll(needRemoveList);
                
                // 合并商品，同步到redis和cookie
                shopcartListRedis.addAll(shopcartListCookie);
                String mergeShopcart = JsonUtils.objectToJson(shopcartListRedis);
                redisOperator.set("shopcart:" + userId,mergeShopcart);
                CookieUtils.setCookie(request,response,"shopcart", mergeShopcart,true);
            }
        }
    }

    private Users setUserNull(Users user) {
        user.setPassword(null);
        user.setBirthday(null);
        user.setCreatedTime(null);
        user.setEmail(null);
        user.setUpdatedTime(null);
        return user;
    }
}
