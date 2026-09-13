package com.example.project.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlogCommunicationTypeEnum {
    FIGHT(0, "send_fight"),
    REVOLUTION(1, "send_revolution");

    public static final BlogCommunicationTypeEnum[] ARRAYS = BlogCommunicationTypeEnum.values();

    private final Integer status;
    private final String name;

    public static boolean isValidStatus(Integer status) {
        for (BlogCommunicationTypeEnum type : ARRAYS) {
            if (type.getStatus().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
