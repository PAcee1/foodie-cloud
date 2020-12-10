package com.pacee1.auth.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>存放账户信息，用户id，token，刷新token等</p>
 *
 * @author : Pace
 * @date : 2020-12-10 15:12
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
    private String userId;

    private String token;

    private String refreshToken;

    private boolean skipVerification = false;
}
