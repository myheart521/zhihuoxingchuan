package com.example.project.module.blog.service.chatmessagesgroup;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.module.blog.controller.admin.chatmessagesgroup.vo.ChatMessagesGroupPageReqVO;
import com.example.project.module.blog.controller.admin.chatmessagesgroup.vo.ChatMessagesGroupSaveReqVO;
import com.example.project.module.blog.dal.dataobject.chatmessagesgroup.ChatMessagesGroupDO;
import com.example.project.module.blog.dal.mysql.chatmessagesgroup.ChatMessagesGroupMapper;
import jakarta.annotation.Resource;
import org.mapstruct.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.CHAT_MESSAGES_GROUP_NOT_EXISTS;

/**
 * 用户聊天记录 Service 实现类
 *

 */
@Service
@Validated
public class ChatMessagesGroupServiceImpl implements ChatMessagesGroupService {

    @Resource
    private ChatMessagesGroupMapper chatMessagesGroupMapper;



    @Override
    public Long createChatMessagesGroup(ChatMessagesGroupSaveReqVO createReqVO) {
        // 插入
        ChatMessagesGroupDO chatMessagesGroup = BeanUtils.toBean(createReqVO, ChatMessagesGroupDO.class);
        chatMessagesGroupMapper.insert(chatMessagesGroup);
        // 返回
        return chatMessagesGroup.getId();
    }

    @Override
    public void updateChatMessagesGroup(ChatMessagesGroupSaveReqVO updateReqVO) {
        // 校验存在
        validateChatMessagesGroupExists(updateReqVO.getId());
        // 更新
        ChatMessagesGroupDO updateObj = BeanUtils.toBean(updateReqVO, ChatMessagesGroupDO.class);
        chatMessagesGroupMapper.updateById(updateObj);
    }

    @Override
    public void deleteChatMessagesGroup(Long id) {
        // 校验存在
        validateChatMessagesGroupExists(id);
        // 删除
        chatMessagesGroupMapper.deleteById(id);
    }

    private void validateChatMessagesGroupExists(Long id) {
        if (chatMessagesGroupMapper.selectById(id) == null) {
            throw exception(CHAT_MESSAGES_GROUP_NOT_EXISTS);
        }
    }

    @Override
    public ChatMessagesGroupDO getChatMessagesGroup(Long id) {
        return chatMessagesGroupMapper.selectById(id);
    }

    @Override
    public PageResult<ChatMessagesGroupDO> getChatMessagesGroupPage(ChatMessagesGroupPageReqVO pageReqVO) {
        return chatMessagesGroupMapper.selectPage(pageReqVO);
    }

    @Override
    @Async
    public void saveChatMessagesGroupAsync(ChatMessagesGroupDO chatMessagesGroupDO) {
            //存入数据库中
            chatMessagesGroupMapper.insert(chatMessagesGroupDO);
    }


}
