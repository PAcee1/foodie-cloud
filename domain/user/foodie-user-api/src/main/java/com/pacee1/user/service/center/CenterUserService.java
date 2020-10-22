package com.pacee1.user.service.center;

import com.pacee1.user.pojo.Users;
import com.pacee1.user.pojo.bo.center.CenterUserBO;
import org.springframework.web.bind.annotation.*;

/**
 * @author pace
 * @version v1.0
 * @Type UserService.java
 * @Desc
 * @date 2020/5/17 15:16
 */
@RestController("center-user-api")
public interface CenterUserService {

    @GetMapping("userInfo")
    Users queryUserInfo(@RequestParam("userId") String userId);

    @PutMapping("userInfo/{userId}")
    Users updateUserInfo(@PathVariable("userId") String userId,
                         @RequestBody CenterUserBO userBO);

    @PostMapping("updateFace")
    Users updateUserFace(@RequestParam("userId") String userId,
                         @RequestParam("faceUrl") String faceUrl);
}
