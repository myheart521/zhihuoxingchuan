package com.example.project.module.blog.dal.dataobject.chatmessagesgroup;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 用户聊天记录 DO
 *

 */
@TableName("blog_chat_messages_group")
@KeySequence("blog_chat_messages_group_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagesGroupDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 发送者ID
     */
    private Long senderId;
    /**
     * 发送者头像
     */
    private String senderAvatar;
    /**
     * 发送者名称
     */
    private String senderName;
    /**
     * 接收的群聊标识
     *
     * 枚举 {@link TODO blog_chat_message_group 对应的类}
     */
    private Integer receiverGroup;
    /**
     * 消息内容
     */
    private String text;
    /**
     * 消息类型
     *
     * 枚举 {@link TODO blog_chat_group_message_type 对应的类}
     */
    private Integer messageType;

}
