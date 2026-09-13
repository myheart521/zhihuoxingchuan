package com.example.project.module.blog.service.comment;

import cn.hutool.core.util.ObjectUtil;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplyPageReqVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplyRespVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplySaveReqVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentRespVO;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import com.example.project.module.blog.dal.dataobject.comment.CommentDO;
import com.example.project.module.blog.dal.dataobject.comment.CommentReplyDO;
import com.example.project.module.blog.dal.mysql.blog.MyPostMapper;
import com.example.project.module.blog.dal.mysql.comment.CommentReplyMapper;
import com.example.project.module.system.dal.dataobject.permission.RoleDO;
import com.example.project.module.system.dal.dataobject.user.AdminUserDO;
import com.example.project.module.system.dal.mysql.user.AdminUserMapper;
import com.example.project.module.system.service.permission.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.COMMENT_REPLY_NOT_EXISTS;

/**
 * 二级评论回复 Service 实现类
 *

 */
@Service
@Validated
public class CommentReplyServiceImpl implements CommentReplyService {

    @Resource
    private CommentReplyMapper commentReplyMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private MyPostMapper myPostMapper;

    @Override
    public Long createCommentReply(CommentReplySaveReqVO createReqVO) {
        // 插入
        CommentReplyDO commentReply = BeanUtils.toBean(createReqVO, CommentReplyDO.class);
        commentReplyMapper.insert(commentReply);
        // 返回
        return commentReply.getId();
    }

    @Override
    public void updateCommentReply(CommentReplySaveReqVO updateReqVO) {
        // 校验存在
        validateCommentReplyExists(updateReqVO.getId());
        // 更新
        CommentReplyDO updateObj = BeanUtils.toBean(updateReqVO, CommentReplyDO.class);
        commentReplyMapper.updateById(updateObj);
    }

    @Override
    public void deleteCommentReply(Long id) {
        // 校验存在
        validateCommentReplyExists(id);
        // 删除
        commentReplyMapper.deleteById(id);
    }


    @Override
    public CommentReplyDO getCommentReply(Long id) {
        return commentReplyMapper.selectById(id);
    }

    @Override
    public PageResult<CommentReplyDO> getCommentReplyPage(CommentReplyPageReqVO pageReqVO) {
        return commentReplyMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CommentReplyRespVO> addCommentReplyInfo(List<CommentReplyDO> commentDOS) {

        List<CommentReplyRespVO> respVOS = new ArrayList<>();
        commentDOS.forEach(commentDO -> {
            CommentReplyRespVO respVO = new CommentReplyRespVO();
            Long userId = commentDO.getUserId();
            //根据id查询
            AdminUserDO userDO = adminUserMapper.selectById(userId);
            if(ObjectUtil.isNotEmpty(userDO)){
                respVO.setUsername(userDO.getUsername());

                //查询角色
                RoleDO roleDO = roleService.getRoleByUserId(userDO.getId());
                respVO.setUserRole(roleDO.getName());
            }

            //查询博客
            MyPostDO myPostDO = myPostMapper.selectById(commentDO.getPostId());
            respVO.setBlogTitle(myPostDO.getTitle());
            respVO.setBlogVisibility(myPostDO.getVisibility().toString());

            //赋值
            BeanUtils.copyProperties(commentDO, respVO);
            respVOS.add(respVO);
        });
        return respVOS;
    }


    private void validateCommentReplyExists(Long id) {
        if (commentReplyMapper.selectById(id) == null) {
            throw exception(COMMENT_REPLY_NOT_EXISTS);
        }
    }

}
