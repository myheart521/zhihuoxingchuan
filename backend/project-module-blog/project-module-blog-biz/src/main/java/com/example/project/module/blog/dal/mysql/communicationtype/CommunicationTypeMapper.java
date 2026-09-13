package com.example.project.module.blog.dal.mysql.communicationtype;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.controller.admin.communicationtype.vo.CommunicationTypePageReqVO;
import com.example.project.module.blog.dal.dataobject.communicationtype.CommunicationTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 群聊类型管理 Mapper
 *

 */
@Mapper
public interface CommunicationTypeMapper extends BaseMapperX<CommunicationTypeDO> {

    default PageResult<CommunicationTypeDO> selectPage(CommunicationTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CommunicationTypeDO>()
                .likeIfPresent(CommunicationTypeDO::getTypeName, reqVO.getTypeName())
                .eqIfPresent(CommunicationTypeDO::getTypeDes, reqVO.getTypeDes())
                .eqIfPresent(CommunicationTypeDO::getTypeImage, reqVO.getTypeImage())
                .betweenIfPresent(CommunicationTypeDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CommunicationTypeDO::getType, reqVO.getType())
                .orderByDesc(CommunicationTypeDO::getId));
    }

}
