package com.example.project.module.blog.service.comment;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.comment.vo.CommentFirstRespVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentPageReqVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentRespVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentSaveReqVO;
import com.example.project.module.blog.dal.dataobject.comment.CommentDO;
import com.example.project.module.blog.dal.dataobject.comment.CommentReplyDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 顶级评论 Service 接口
 *

 */
public interface CommentService {

    /**
     * 创建顶级评论
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createComment(@Valid CommentSaveReqVO createReqVO);

    /**
     * 更新顶级评论
     *
     * @param updateReqVO 更新信息
     */
    void updateComment(@Valid CommentSaveReqVO updateReqVO);

    /**
     * 删除顶级评论
     *
     * @param id 编号
     */
    void deleteComment(Long id);

    /**
     * 获得顶级评论
     *
     * @param id 编号
     * @return 顶级评论
     */
    CommentDO getComment(Long id);

    /**
     * 获得顶级评论分页
     *
     * @param pageReqVO 分页查询
     * @return 顶级评论分页
     */
    PageResult<CommentDO> getCommentPage(CommentPageReqVO pageReqVO);

    // ==================== 子表（二级评论回复） ====================

    /**
     * 获得二级评论回复列表
     *
     * @param parentId 关联的顶级评论ID
     * @return 二级评论回复列表
     */
    List<CommentReplyDO> getCommentReplyListByParentId(Long parentId);


    //添加用户名和用户角色的方法、还有博客信息
    List<CommentRespVO> addCommentInfo(List<CommentDO> commentDOS);



    PageResult<CommentDO> getCommentByBlog(CommentPageReqVO pageReqVO);
}
