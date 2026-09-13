package com.example.project.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlogMessageTypeEnum {
    TEXT(0, "文字"),
    IMAGE(1, "图片"),
    FILE(2, "文件");
    public static final BlogMessageTypeEnum[] ARRAYS = BlogMessageTypeEnum.values();

    private final Integer status;
    private final String name;

    public static boolean isValidStatus(Integer status) {
        for (BlogMessageTypeEnum type : ARRAYS) {
            if (type.getStatus().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
