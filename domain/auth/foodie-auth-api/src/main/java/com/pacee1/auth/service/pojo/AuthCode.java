package com.pacee1.auth.service.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>鉴权状态码</p>
 *
 * @author : Pace
 * @date : 2020-12-10 15:12
 **/
@AllArgsConstructor
public enum AuthCode {

    SUCCESS(1L),
    USER_NOT_FOUND(1000L),
    INVALID_CREDENTIAL(2000L);

    @Getter
    private Long code;

}
