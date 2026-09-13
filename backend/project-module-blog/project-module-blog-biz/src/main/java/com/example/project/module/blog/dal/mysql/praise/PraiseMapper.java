package com.example.project.module.blog.dal.mysql.praise;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.controller.admin.praise.vo.PraisePageReqVO;
import com.example.project.module.blog.dal.dataobject.praise.PraiseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客点赞 Mapper
 *

 */
@Mapper
public interface PraiseMapper extends BaseMapperX<PraiseDO> {

    default PageResult<PraiseDO> selectPage(PraisePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PraiseDO>()
                .eqIfPresent(PraiseDO::getBlogId, reqVO.getBlogId())
                .eqIfPresent(PraiseDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(PraiseDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PraiseDO::getId));
    }

}
