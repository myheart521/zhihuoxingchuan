package com.example.project.module.blog.controller.admin.comment.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 顶级评论分页 Request VO")
@Data
@ToString(callSuper = true)
public class CommentFirstRespVO {
    @Schema(description = "评论ID（顶级）", requiredMode = Schema.RequiredMode.REQUIRED, example = "18181")
    @ExcelProperty("评论ID（顶级）")
    private Long id;

    @Schema(description = "关联的博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12650")
    @ExcelProperty("关联的博客ID")
    private Long postId;

    @Schema(description = "博客标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "12650")
    @ExcelProperty("博客标题")
    private String blogTitle;

    @Schema(description = "评论者用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22465")
    @ExcelProperty("评论者用户ID")
    private Long userId;

    @Schema(description = "评论者账户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "22465")
    @ExcelProperty("评论者账户名")
    private String username;

    @Schema(description = "评论者真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "22465")
    @ExcelProperty("评论者真实姓名")
    private String realName;

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("评论内容")
    private String content;

    @Schema(description = "状态：0-待审核 1-已发布 2-已删除", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态：0-待审核 1-已发布 2-已删除")
    private Integer status;

    @Schema(description = "审核人ID（管理员）", example = "3888")
    @ExcelProperty("审核人ID（管理员）")
    private Long auditUserId;

    @Schema(description = "审核人账户名", example = "帅比")
    @ExcelProperty("审核人账户名")
    private String auditUsername;

    @Schema(description = "审核人角色", example = "超级管理员")
    @ExcelProperty("审核人账户名")
    private String auditRole;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
