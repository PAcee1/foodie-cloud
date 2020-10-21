package com.pacee1.enums;

/**
 * @author pace
 * @version v1.0
 * @Type Sex.java
 * @Desc
 * @date 2020/5/17 16:47
 */
public enum CommentLevel {
    GOOD(1,"好评"),
    NORMAL(2,"中评"),
    BAD(3,"差评");

    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
