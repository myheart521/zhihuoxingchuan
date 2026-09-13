package com.example.project.module.blog.controller.admin.websocket.communication.message;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceiveMessage {

    /**
     * 发送人头像
     */
    private String userAvatar;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 发送人的id
     */
    private Long userId;

    /**
     * 内容
     */
    private String text;

}
