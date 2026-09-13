package com.example.project.module.blog.controller.admin.comment;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.excel.core.util.ExcelUtils;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplyPageReqVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplyRespVO;
import com.example.project.module.blog.controller.admin.comment.vo.CommentReplySaveReqVO;
import com.example.project.module.blog.dal.dataobject.comment.CommentReplyDO;
import com.example.project.module.blog.service.comment.CommentReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.example.project.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.example.project.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 二级评论回复")
@RestController
@RequestMapping("/blog/comment-reply")
@Validated
public class CommentReplyController {

    @Resource
    private CommentReplyService commentReplyService;

    @PostMapping("/create")
    public CommonResult<Long> createCommentReply(@Valid @RequestBody CommentReplySaveReqVO createReqVO) {
        return success(commentReplyService.createCommentReply(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新二级评论回复")
    @PreAuthorize("@ss.hasPermission('blog:comment-reply:update')")
    public CommonResult<Boolean> updateCommentReply(@Valid @RequestBody CommentReplySaveReqVO updateReqVO) {
        commentReplyService.updateCommentReply(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除二级评论回复")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCommentReply(@RequestParam("id") Long id) {
        commentReplyService.deleteCommentReply(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得二级评论回复")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<CommentReplyRespVO> getCommentReply(@RequestParam("id") Long id) {
        CommentReplyDO commentReply = commentReplyService.getCommentReply(id);
        return success(BeanUtils.toBean(commentReply, CommentReplyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得二级评论回复分页")
    public CommonResult<PageResult<CommentReplyRespVO>> getCommentReplyPage(@Valid CommentReplyPageReqVO pageReqVO) {
        PageResult<CommentReplyDO> pageResult = commentReplyService.getCommentReplyPage(pageReqVO);
        List<CommentReplyRespVO> commentReplyRespVOS = commentReplyService.addCommentReplyInfo(pageResult.getList());
        PageResult<CommentReplyRespVO> pageResult1 = new PageResult<>(commentReplyRespVOS, pageResult.getTotal());
        return success(pageResult1);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出二级评论回复 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCommentReplyExcel(@Valid CommentReplyPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CommentReplyDO> list = commentReplyService.getCommentReplyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "二级评论回复.xls", "数据", CommentReplyRespVO.class,
                        BeanUtils.toBean(list, CommentReplyRespVO.class));
    }

}
