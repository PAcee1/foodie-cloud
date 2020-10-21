package com.pacee1.enums;

/**
 * @author pace
 * @version v1.0
 * @Type Sex.java
 * @Desc
 * @date 2020/5/17 16:47
 */
public enum YesOrNo {
    NO(0,"否"),
    YES(1,"是");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
