package com.example.project.module.blog.dal.mysql.accuse;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.controller.admin.accuse.vo.AccusePageReqVO;
import com.example.project.module.blog.dal.dataobject.accuse.AccuseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客举报记录 Mapper
 *

 */
@Mapper
public interface AccuseMapper extends BaseMapperX<AccuseDO> {

    default PageResult<AccuseDO> selectPage(AccusePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AccuseDO>()
                .eqIfPresent(AccuseDO::getPostId, reqVO.getPostId())
                .eqIfPresent(AccuseDO::getUserId, reqVO.getUserId())
                .eqIfPresent(AccuseDO::getType, reqVO.getType())
                .eqIfPresent(AccuseDO::getContent, reqVO.getContent())
                .eqIfPresent(AccuseDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AccuseDO::getHandleUserId, reqVO.getHandleUserId())
                .betweenIfPresent(AccuseDO::getHandleTime, reqVO.getHandleTime())
                .betweenIfPresent(AccuseDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AccuseDO::getId));
    }

}
