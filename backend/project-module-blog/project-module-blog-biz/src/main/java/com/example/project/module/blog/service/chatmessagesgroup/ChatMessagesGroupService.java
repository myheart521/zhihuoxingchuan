package com.example.project.module.blog.service.chatmessagesgroup;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.chatmessagesgroup.vo.ChatMessagesGroupPageReqVO;
import com.example.project.module.blog.controller.admin.chatmessagesgroup.vo.ChatMessagesGroupSaveReqVO;
import com.example.project.module.blog.dal.dataobject.chatmessagesgroup.ChatMessagesGroupDO;
import jakarta.validation.Valid;

/**
 * 用户聊天记录 Service 接口
 *

 */
public interface ChatMessagesGroupService {

    /**
     * 创建用户聊天记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createChatMessagesGroup(@Valid ChatMessagesGroupSaveReqVO createReqVO);

    /**
     * 更新用户聊天记录
     *
     * @param updateReqVO 更新信息
     */
    void updateChatMessagesGroup(@Valid ChatMessagesGroupSaveReqVO updateReqVO);

    /**
     * 删除用户聊天记录
     *
     * @param id 编号
     */
    void deleteChatMessagesGroup(Long id);

    /**
     * 获得用户聊天记录
     *
     * @param id 编号
     * @return 用户聊天记录
     */
    ChatMessagesGroupDO getChatMessagesGroup(Long id);

    /**
     * 获得用户聊天记录分页
     *
     * @param pageReqVO 分页查询
     * @return 用户聊天记录分页
     */
    PageResult<ChatMessagesGroupDO> getChatMessagesGroupPage(ChatMessagesGroupPageReqVO pageReqVO);


    /**
     * 异步将消息存储到数据库使用TaskExecutor
     */
    void saveChatMessagesGroupAsync(ChatMessagesGroupDO chatMessagesGroupDO);
}
