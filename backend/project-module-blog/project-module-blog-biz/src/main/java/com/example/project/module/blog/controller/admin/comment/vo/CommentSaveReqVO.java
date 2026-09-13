package com.example.project.module.blog.controller.admin.comment.vo;

import com.example.project.module.blog.dal.dataobject.comment.CommentReplyDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 顶级评论新增/修改 Request VO")
@Data
public class CommentSaveReqVO {

    @Schema(description = "评论ID（顶级）", requiredMode = Schema.RequiredMode.REQUIRED, example = "18181")
    private Long id;

    @Schema(description = "关联的博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12650")
    @NotNull(message = "关联的博客ID不能为空")
    private Long postId;

    @Schema(description = "评论者用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22465")
    @NotNull(message = "评论者用户ID不能为空")
    private Long userId;

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "评论内容不能为空")
    private String content;

    @Schema(description = "状态：0-待审核 1-已发布 2-已删除", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态：0-待审核 1-已发布 2-已删除不能为空")
    private Integer status;

    @Schema(description = "审核人ID（管理员）", example = "3888")
    private Long auditUserId;

    @Schema(description = "二级评论回复列表")
    private List<CommentReplyDO> commentReplys;

}
