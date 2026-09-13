package com.example.project.module.blog.controller.admin.websocket.communication;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.project.framework.common.enums.BlogCommunicationTypeEnum;
import com.example.project.framework.common.enums.BlogMessageTypeEnum;
import com.example.project.framework.common.enums.UserTypeEnum;
import com.example.project.framework.websocket.core.listener.WebSocketMessageListener;
import com.example.project.framework.websocket.core.sender.WebSocketMessageSender;
import com.example.project.framework.websocket.core.util.WebSocketFrameworkUtils;
import com.example.project.module.blog.controller.admin.websocket.communication.message.ReceiveMessage;
import com.example.project.module.blog.controller.admin.websocket.communication.message.SendMessage;
import com.example.project.module.blog.dal.dataobject.chatmessagesgroup.ChatMessagesGroupDO;
import com.example.project.module.blog.service.chatmessagesgroup.ChatMessagesGroupService;
import com.example.project.module.system.dal.dataobject.user.AdminUserDO;
import com.example.project.module.system.service.user.AdminUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;

@Component
public class FightWSMessageListener implements WebSocketMessageListener<SendMessage> {
    @Resource
    private WebSocketMessageSender webSocketMessageSender;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private ChatMessagesGroupService chatMessagesGroupService;

    @Override
    public void onMessage(WebSocketSession session, SendMessage message) {

        Long fromUserId = WebSocketFrameworkUtils.getLoginUserId(session);
        //获取用户头像
        AdminUserDO user = adminUserService.getUser(fromUserId);
        Integer messageType = message.getType();
        if (ObjectUtil.isEmpty(messageType)) {
            //默认文本类型
            messageType = BlogMessageTypeEnum.TEXT.getStatus();
        }
        //将内容发送给全部人
        ReceiveMessage toMessage = new ReceiveMessage().setUserId(fromUserId)
                .setText(message.getText()).setUserAvatar(user.getAvatar())
                .setUserName(user.getUsername()).setTime(LocalDateTime.now());
        ChatMessagesGroupDO chatMessagesGroupDO = new ChatMessagesGroupDO();
        chatMessagesGroupDO.setReceiverGroup(BlogCommunicationTypeEnum.FIGHT.getStatus())
                .setSenderId(fromUserId)
                .setSenderAvatar(user.getAvatar())
                .setText(message.getText())
                .setSenderAvatar(user.getAvatar())
                .setSenderName(user.getUsername())
                .setMessageType(messageType);
        //使用spring的线程池，异步存储到数据库
        chatMessagesGroupService.saveChatMessagesGroupAsync(chatMessagesGroupDO);
        webSocketMessageSender.sendObject(UserTypeEnum.ADMIN.getValue(), //给所有用户
                "receive_fight", toMessage);
    }

    @Override
    public String getType() {
        return "send_fight";
    }
}
