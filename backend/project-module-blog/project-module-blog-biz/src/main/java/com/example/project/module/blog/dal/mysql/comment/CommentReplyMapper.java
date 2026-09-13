package com.example.project.module.blog.dal.mysql.comment;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplyPageReqVO;
import com.example.project.module.blog.dal.dataobject.comment.CommentReplyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 二级评论回复 Mapper
 *

 */
@Mapper
public interface CommentReplyMapper extends BaseMapperX<CommentReplyDO> {

    default List<CommentReplyDO> selectListByParentId(Long parentId) {
        return selectList(CommentReplyDO::getParentId, parentId);
    }

    default int deleteByParentId(Long parentId) {
        return delete(CommentReplyDO::getParentId, parentId);
    }

    default PageResult<CommentReplyDO> selectPage(CommentReplyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CommentReplyDO>()
                .eqIfPresent(CommentReplyDO::getParentId, reqVO.getParentId())
                .eqIfPresent(CommentReplyDO::getPostId, reqVO.getPostId())
                .eqIfPresent(CommentReplyDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CommentReplyDO::getReplyToUserId, reqVO.getReplyToUserId())
                .eqIfPresent(CommentReplyDO::getContent, reqVO.getContent())
                .eqIfPresent(CommentReplyDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CommentReplyDO::getAuditUserId, reqVO.getAuditUserId())
                .betweenIfPresent(CommentReplyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CommentReplyDO::getId));
    }

}
