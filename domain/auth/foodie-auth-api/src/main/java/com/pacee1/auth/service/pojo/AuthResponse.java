package com.pacee1.auth.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>鉴权响应对象</p>
 *
 * @author : Pace
 * @date : 2020-12-10 15:14
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Account account;

    private Long code;
}
