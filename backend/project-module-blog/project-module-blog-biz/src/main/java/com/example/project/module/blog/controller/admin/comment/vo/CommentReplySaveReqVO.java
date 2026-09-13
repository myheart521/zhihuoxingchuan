package com.example.project.module.blog.controller.admin.comment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 二级评论回复新增/修改 Request VO")
@Data
public class CommentReplySaveReqVO {

    @Schema(description = "回复ID（二级）", requiredMode = Schema.RequiredMode.REQUIRED, example = "25345")
    private Long id;

    @Schema(description = "关联的顶级评论ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23647")
    @NotNull(message = "关联的顶级评论ID不能为空")
    private Long parentId;

    @Schema(description = "冗余博客ID（避免跨表查文章）", requiredMode = Schema.RequiredMode.REQUIRED, example = "5960")
    @NotNull(message = "冗余博客ID（避免跨表查文章）不能为空")
    private Long postId;

    @Schema(description = "回复者用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13414")
    @NotNull(message = "回复者用户ID不能为空")
    private Long userId;

    @Schema(description = "被回复的用户ID（可空，默认回复父级评论人）", example = "22617")
    private Long replyToUserId;

    @Schema(description = "回复内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "回复内容不能为空")
    private String content;

    @Schema(description = "状态：0-待审核 1-已发布 2-已删除", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态：0-待审核 1-已发布 2-已删除不能为空")
    private Integer status;

    @Schema(description = "审核人ID（管理员）", example = "27799")
    private Long auditUserId;

}
