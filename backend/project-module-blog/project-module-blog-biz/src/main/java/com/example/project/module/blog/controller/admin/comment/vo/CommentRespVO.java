package com.example.project.module.blog.controller.admin.comment.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 顶级评论 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CommentRespVO {

    @Schema(description = "评论ID（顶级）", requiredMode = Schema.RequiredMode.REQUIRED, example = "18181")
    @ExcelProperty("评论ID（顶级）")
    private Long id;

    @Schema(description = "关联的博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12650")
    @ExcelProperty("关联的博客ID")
    private Long postId;

    @Schema(description = "评论者用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22465")
    @ExcelProperty("评论者用户ID")
    private Long userId;

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("评论内容")
    private String content;

    @Schema(description = "状态：0-待审核 1-已发布 2-已删除", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态：0-待审核 1-已发布 2-已删除")
    private Integer status;

    @Schema(description = "审核人ID（管理员）", example = "3888")
    @ExcelProperty("审核人ID（管理员）")
    private Long auditUserId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    //新加入
    @Schema(description = "评论者用户姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "帅比")
    @ExcelProperty("评论者用户姓名")
    private String username;

    @Schema(description = "评论者用户身份:超级管理员、博客管理员、博客审核用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "博客管理员")
    @ExcelProperty("评论者用户姓名")
    private String userRole;

    @Schema(description = "博客标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "springboot")
    @ExcelProperty("博客标题")
    private String blogTitle;

    @Schema(description = "博客可见度0-公开 1-仅自己 2-粉丝可见", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("博客可见度")
    private String blogVisibility;
}
