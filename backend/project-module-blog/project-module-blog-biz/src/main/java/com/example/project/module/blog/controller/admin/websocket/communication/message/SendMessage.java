package com.example.project.module.blog.controller.admin.websocket.communication.message;

import lombok.Data;

@Data
public class SendMessage {

    /**
     * 发送人id
     */
    private Long userId;
    /**
     * 内容
     */
    private String text;

    /**
     * 内容类型
     */
    private Integer type;
}
