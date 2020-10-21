package com.pacee1.enums;

/**
 * @author pace
 * @version v1.0
 * @Type Sex.java
 * @Desc
 * @date 2020/5/17 16:47
 */
public enum PayMethod {
    WX(1,"微信"),
    ZFB(2,"支付宝");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
