package com.example.project.module.blog.dal.mysql.comment;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.security.core.util.SecurityFrameworkUtils;
import com.example.project.module.blog.controller.admin.comment.vo.CommentPageReqVO;
import com.example.project.module.blog.dal.dataobject.comment.CommentDO;
import com.example.project.module.blog.dal.mysql.blog.MyPostMapper;
import com.example.project.module.blog.service.post.MyPostService;
import org.apache.ibatis.annotations.Mapper;

import jakarta.annotation.Resource;

/**
 * 顶级评论 Mapper
 *

 */
@Mapper
public interface CommentMapper extends BaseMapperX<CommentDO> {


    default PageResult<CommentDO> selectPage(CommentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CommentDO>()
                .eqIfPresent(CommentDO::getPostId, reqVO.getPostId())
                .eqIfPresent(CommentDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CommentDO::getContent, reqVO.getContent())
                .eqIfPresent(CommentDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CommentDO::getAuditUserId, reqVO.getAuditUserId())
                .betweenIfPresent(CommentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CommentDO::getId));
    }

    //获取用户个人博客的评论分页
//    default PageResult<CommentDO> selectPageByUserBlogId(CommentPageReqVO reqVO) {
//        //获取用户发的博客id
//        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
//
//        //获取某个博客下的评论
//        LambdaQueryWrapperX<CommentDO> wrapper = new LambdaQueryWrapperX<CommentDO>();
//        wrapper.eq(CommentDO::getPostId, reqVO.getPostId());
//    }

}
