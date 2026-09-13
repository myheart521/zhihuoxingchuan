package com.example.project.module.blog.service.comment;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplyPageReqVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplyRespVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplySaveReqVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentRespVO;
import com.example.project.module.blog.dal.dataobject.comment.CommentDO;
import com.example.project.module.blog.dal.dataobject.comment.CommentReplyDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 二级评论回复 Service 接口
 *

 */
public interface CommentReplyService {

    /**
     * 创建二级评论回复
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCommentReply(@Valid CommentReplySaveReqVO createReqVO);

    /**
     * 更新二级评论回复
     *
     * @param updateReqVO 更新信息
     */
    void updateCommentReply(@Valid CommentReplySaveReqVO updateReqVO);

    /**
     * 删除二级评论回复
     *
     * @param id 编号
     */
    void deleteCommentReply(Long id);

    /**
     * 获得二级评论回复
     *
     * @param id 编号
     * @return 二级评论回复
     */
    CommentReplyDO getCommentReply(Long id);

    /**
     * 获得二级评论回复分页
     *
     * @param pageReqVO 分页查询
     * @return 二级评论回复分页
     */
    PageResult<CommentReplyDO> getCommentReplyPage(CommentReplyPageReqVO pageReqVO);
    //添加用户名和用户角色的方法
    List<CommentReplyRespVO> addCommentReplyInfo(List<CommentReplyDO> commentDOS);


}
