package com.example.project.module.blog.dal.mysql.chatmessagesgroup;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.controller.admin.chatmessagesgroup.vo.ChatMessagesGroupPageReqVO;
import com.example.project.module.blog.dal.dataobject.chatmessagesgroup.ChatMessagesGroupDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户聊天记录 Mapper
 *

 */
@Mapper
public interface ChatMessagesGroupMapper extends BaseMapperX<ChatMessagesGroupDO> {

    default PageResult<ChatMessagesGroupDO> selectPage(ChatMessagesGroupPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ChatMessagesGroupDO>()
                .likeIfPresent(ChatMessagesGroupDO::getSenderName, reqVO.getSenderName())
                .eqIfPresent(ChatMessagesGroupDO::getReceiverGroup, reqVO.getReceiverGroup())
                .eqIfPresent(ChatMessagesGroupDO::getText, reqVO.getText())
                .eqIfPresent(ChatMessagesGroupDO::getMessageType, reqVO.getMessageType())
                .betweenIfPresent(ChatMessagesGroupDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ChatMessagesGroupDO::getId));
    }

}
