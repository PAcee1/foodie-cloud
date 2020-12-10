package com.pacee1.auth.service;

import com.pacee1.auth.service.pojo.Account;
import com.pacee1.auth.service.pojo.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-12-10 15:10
 **/
@FeignClient("foodie-auth-service")
@RequestMapping("auth-service")
public interface AuthService {
    @PostMapping("token")
    public AuthResponse tokenize(@RequestParam("userId") String userId);

    @PostMapping("verify")
    public AuthResponse verify(@RequestBody Account account);

    @PostMapping("refresh")
    public AuthResponse refresh(@RequestParam("refresh") String refresh);

    @DeleteMapping("delete")
    public AuthResponse delete(@RequestBody Account account);
}
