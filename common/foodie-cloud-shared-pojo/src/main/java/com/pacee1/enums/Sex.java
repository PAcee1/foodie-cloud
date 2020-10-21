package com.pacee1.enums;

/**
 * @author pace
 * @version v1.0
 * @Type Sex.java
 * @Desc
 * @date 2020/5/17 16:47
 */
public enum Sex {
    WOMAN(0,"女"),
    MAN(1,"男"),
    SECRET(2,"保密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
