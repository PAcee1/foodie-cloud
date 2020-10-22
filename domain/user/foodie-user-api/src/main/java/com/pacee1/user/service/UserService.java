package com.pacee1.user.service;

import com.pacee1.user.pojo.Users;
import com.pacee1.user.pojo.bo.UserBO;
import org.springframework.web.bind.annotation.*;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@RestController("user-api")
public interface UserService {

    // 判断用户名是否存在
    @GetMapping("usernameIsExist")
    boolean queryUsernameIsExist(@RequestParam("userName") String username);

    // 创建用户
    @PostMapping("user")
    Users createUser(@RequestBody UserBO userBO);

    @GetMapping("verify")
    Users queryUserForLogin(@RequestParam("userName") String username,
                            @RequestParam("password") String password);
}
