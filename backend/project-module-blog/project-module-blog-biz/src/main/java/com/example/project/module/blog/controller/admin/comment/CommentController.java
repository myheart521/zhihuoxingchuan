package com.example.project.module.blog.controller.admin.comment;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.excel.core.util.ExcelUtils;
import com.example.project.framework.security.core.util.SecurityFrameworkUtils;
import com.example.project.module.blog.controller.admin.comment.vo.*;
import com.example.project.module.blog.dal.dataobject.comment.CommentDO;
import com.example.project.module.blog.dal.dataobject.comment.CommentReplyDO;
import com.example.project.module.blog.service.blog.BlogService;
import com.example.project.module.blog.service.comment.CommentService;
import com.example.project.module.system.dal.dataobject.permission.RoleDO;
import com.example.project.module.system.enums.permission.RoleCodeEnum;
import com.example.project.module.system.service.permission.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.project.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.example.project.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 顶级评论")
@RestController
@RequestMapping("/blog/comment")
@Validated
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private RoleService roleService;

    @Resource
    private BlogService blogService;

    @PostMapping("/create")
    @Operation(summary = "创建顶级评论")
    public CommonResult<Long> createComment(@Valid @RequestBody CommentSaveReqVO createReqVO) {
        return success(commentService.createComment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新顶级评论")
    @PreAuthorize("@ss.hasPermission('blog:comment:update')")
    public CommonResult<Boolean> updateComment(@Valid @RequestBody CommentSaveReqVO updateReqVO) {
        commentService.updateComment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除顶级评论")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteComment(@RequestParam("id") Long id) {
        commentService.deleteComment(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得顶级评论")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<CommentRespVO> getComment(@RequestParam("id") Long id) {
        CommentDO comment = commentService.getComment(id);
        return success(BeanUtils.toBean(comment, CommentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得顶级评论分页")
    public CommonResult<PageResult<CommentRespVO>> getCommentPage(@Valid CommentPageReqVO pageReqVO) {
        PageResult<CommentDO> pageResult = commentService.getCommentPage(pageReqVO);
        //添加用户名和用户角色
        RoleDO role = roleService.getRoleByUserId(SecurityFrameworkUtils.getLoginUserId());
//        if (role.getCode().equals(RoleCodeEnum.SUPER_ADMIN.getCode()) || role.getCode().equals(RoleCodeEnum.BLOG_MANAGE.getCode())) {
//            PageResult<CommentRespVO> bean = BeanUtils.toBean(pageResult, CommentRespVO.class);
        List<CommentRespVO> respVOS = commentService.addCommentInfo(pageResult.getList());
        PageResult<CommentRespVO> bean = new PageResult<>(respVOS, pageResult.getTotal());
        return success(bean);
//        }
//        //否则只返回个人博客的评论
//        List<CommentDO> list = pageResult.getList();
//
//        List<CommentDO> newList = new ArrayList<>();
//        for (CommentDO commentDO : list) {
//            Long userIdByBlogId = blogService.getUserIdByBlogId(commentDO.getPostId());
//            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
//            if(userIdByBlogId.equals(loginUserId)){
//                newList.add(commentDO);
//            }
//        }
//
////        list.removeIf(commentDO -> !blogService.getUserIdByBlogId(commentDO.getPostId()).equals(SecurityFrameworkUtils.getLoginUserId()));
////        PageResult<CommentRespVO> bean = BeanUtils.toBean(pageResult, CommentRespVO.class);
//        List<CommentRespVO> respVOS = commentService.addCommentInfo(newList);
//        PageResult<CommentRespVO> bean = new PageResult<>(respVOS, pageResult.getTotal());
//        return success(bean);
    }
    @GetMapping("/user/page")
    @Operation(summary = "获得个人评论分页")
    public CommonResult<PageResult<CommentRespVO>> getUserCommentPage(@Valid CommentPageReqVO pageReqVO) {
        pageReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        PageResult<CommentDO> pageResult = commentService.getCommentPage(pageReqVO);
        List<CommentRespVO> respVOS = commentService.addCommentInfo(pageResult.getList());
        PageResult<CommentRespVO> bean = new PageResult<>(respVOS, pageResult.getTotal());
        return success(bean);
    }

    @GetMapping("/user/blog/page")
    @Operation(summary = "获得用户博客的评论分页")
    public CommonResult<PageResult<CommentRespVO>> getUserBlogCommentPage(@Valid CommentPageReqVO pageReqVO) {
        pageReqVO.setUserId(SecurityFrameworkUtils.getLoginUserId());
        PageResult<CommentDO> pageResult= commentService.getCommentByBlog(pageReqVO);
        List<CommentRespVO> respVOS = commentService.addCommentInfo(pageResult.getList());
        PageResult<CommentRespVO> bean = new PageResult<>(respVOS, pageResult.getTotal());
        return success(bean);
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出顶级评论 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCommentExcel(@Valid CommentPageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CommentDO> list = commentService.getCommentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "顶级评论.xls", "数据", CommentRespVO.class,
                BeanUtils.toBean(list, CommentRespVO.class));
    }

    // ==================== 子表（二级评论回复） ====================

    @GetMapping("/comment-reply/list-by-parent-id")
    @Operation(summary = "获得二级评论回复列表")
    @Parameter(name = "parentId", description = "关联的顶级评论ID")
    public CommonResult<List<CommentReplyDO>> getCommentReplyListByParentId(@RequestParam("parentId") Long parentId) {
        return success(commentService.getCommentReplyListByParentId(parentId));
    }

}
