package com.example.project.module.blog.service.comment;

import cn.hutool.core.util.ObjectUtil;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.security.core.util.SecurityFrameworkUtils;
import com.example.project.module.blog.controller.admin.comment.vo.CommentFirstRespVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentPageReqVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentRespVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentSaveReqVO;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import com.example.project.module.blog.dal.dataobject.comment.CommentDO;
import com.example.project.module.blog.dal.dataobject.comment.CommentReplyDO;
import com.example.project.module.blog.dal.mysql.blog.MyPostMapper;
import com.example.project.module.blog.dal.mysql.comment.CommentMapper;
import com.example.project.module.blog.dal.mysql.comment.CommentReplyMapper;
import com.example.project.module.blog.service.blog.BlogService;
import com.example.project.module.blog.service.post.MyPostService;
import com.example.project.module.system.dal.dataobject.permission.RoleDO;
import com.example.project.module.system.dal.dataobject.user.AdminUserDO;
import com.example.project.module.system.dal.mysql.user.AdminUserMapper;
import com.example.project.module.system.service.permission.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.COMMENT_NOT_EXISTS;

/**
 * 顶级评论 Service 实现类
 *

 */
@Service
@Validated
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CommentReplyMapper commentReplyMapper;

    @Resource
    private MyPostMapper myPostMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private BlogService blogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createComment(CommentSaveReqVO createReqVO) {
        // 插入
        CommentDO comment = BeanUtils.toBean(createReqVO, CommentDO.class);
        commentMapper.insert(comment);
        if (createReqVO.getCommentReplys() == null) {
            createReqVO.setCommentReplys(new ArrayList<>());
        }
        // 插入子表
        createCommentReplyList(comment.getId(), createReqVO.getCommentReplys());
        // 返回
        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateComment(CommentSaveReqVO updateReqVO) {
        // 校验存在
        validateCommentExists(updateReqVO.getId());
        // 更新
        CommentDO updateObj = BeanUtils.toBean(updateReqVO, CommentDO.class);
        commentMapper.updateById(updateObj);

        // 更新子表
        updateCommentReplyList(updateReqVO.getId(), updateReqVO.getCommentReplys());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long id) {
        // 校验存在
        validateCommentExists(id);
        // 删除
        commentMapper.deleteById(id);

        // 删除子表
        deleteCommentReplyByParentId(id);
    }

    private void validateCommentExists(Long id) {
        if (commentMapper.selectById(id) == null) {
            throw exception(COMMENT_NOT_EXISTS);
        }
    }

    @Override
    public CommentDO getComment(Long id) {
        return commentMapper.selectById(id);
    }

    @Override
    public PageResult<CommentDO> getCommentPage(CommentPageReqVO pageReqVO) {
        return commentMapper.selectPage(pageReqVO);
//        List<CommentDO> commentDOS = commentDOPageResult.getList();
//        List<Long> postIds = commentDOS.stream().map(CommentDO::getPostId).collect(Collectors.toList());
//        List<MyPostDO> blogs = myPostMapper.selectByIds(postIds);
//        List<AdminUserDO> userDOS = adminUserMapper.selectByIds(commentDOS.stream().map(CommentDO::getUserId).collect(Collectors.toList()));
//        List<AdminUserDO> auditorDOS = adminUserMapper.selectByIds(commentDOS.stream().map(CommentDO::getAuditUserId).collect(Collectors.toList()));
//        //填充博客、评论者、审核人的一些信息
//        List<CommentFirstRespVO> commentFirstRespVOS = new ArrayList<>();
//        for (int i = 0; i < commentDOS.size(); i++) {
//            CommentFirstRespVO commentFirstRespVO = new CommentFirstRespVO();
//            BeanUtils.copyProperties(commentDOS.get(i), commentFirstRespVO);
//            //设置博客相关
//            MyPostDO blog = blogs.get(i);
//            commentFirstRespVO.setBlogTitle(blog.getTitle());
//            //设置评论用户相关
//            AdminUserDO userDO = userDOS.get(i);
//            commentFirstRespVO.setUsername(userDO.getUsername());
//            commentFirstRespVO.setRealName(userDO.getRealName());
//            //设置审核人相关
//            AdminUserDO auditorDO = auditorDOS.get(i);
//            commentFirstRespVO.setAuditUsername(auditorDO.getUsername());
//            RoleDO role = roleService.getRole(auditorDO.getId());
//            commentFirstRespVO.setRealName(role.getName());
//            commentFirstRespVOS.add(commentFirstRespVO);
//        }
//        return new PageResult<>(commentFirstRespVOS, commentDOPageResult.getTotal());
    }

    // ==================== 子表（二级评论回复） ====================

    @Override
    public List<CommentReplyDO> getCommentReplyListByParentId(Long parentId) {
        return commentReplyMapper.selectListByParentId(parentId);
    }

    @Override
    public List<CommentRespVO> addCommentInfo(List<CommentDO> commentDOS) {

        List<CommentRespVO> respVOS = new ArrayList<>();
        commentDOS.forEach(commentDO -> {
            CommentRespVO respVO = new CommentRespVO();
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
            if(ObjectUtil.isNotEmpty(myPostDO)){
                respVO.setBlogTitle(myPostDO.getTitle());
                respVO.setBlogVisibility(myPostDO.getVisibility().toString());
            }
            //赋值
            BeanUtils.copyProperties(commentDO, respVO);
            respVOS.add(respVO);
        });
        return respVOS;
    }

    @Override
    public PageResult<CommentDO> getCommentByBlog(CommentPageReqVO pageReqVO) {
        //获取用户发的博客id
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        List<MyPostDO> blogs = blogService.getBlogByUserId(loginUserId);
        //获取某个博客下的评论
        List<Long> blogIds = blogs.stream().map(MyPostDO::getId).collect(Collectors.toList());
        if(ObjectUtil.isEmpty(blogIds)){
            return new PageResult<>();
        }
        LambdaQueryWrapperX<CommentDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.inIfPresent(CommentDO::getPostId, blogIds);
        return commentMapper.selectPage(pageReqVO, queryWrapper);
    }

    private void createCommentReplyList(Long parentId, List<CommentReplyDO> list) {
        list.forEach(o -> o.setParentId(parentId));
        commentReplyMapper.insertBatch(list);
    }

    private void updateCommentReplyList(Long parentId, List<CommentReplyDO> list) {
        deleteCommentReplyByParentId(parentId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createCommentReplyList(parentId, list);
    }

    private void deleteCommentReplyByParentId(Long parentId) {
        commentReplyMapper.deleteByParentId(parentId);
    }

}
