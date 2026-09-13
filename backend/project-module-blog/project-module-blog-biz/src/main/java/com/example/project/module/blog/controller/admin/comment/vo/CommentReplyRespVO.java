package com.example.project.module.blog.controller.admin.comment.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 二级评论回复 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CommentReplyRespVO {

    @Schema(description = "回复ID（二级）", requiredMode = Schema.RequiredMode.REQUIRED, example = "25345")
    @ExcelProperty("回复ID（二级）")
    private Long id;

    @Schema(description = "关联的顶级评论ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23647")
    @ExcelProperty("关联的顶级评论ID")
    private Long parentId;

    @Schema(description = "冗余博客ID（避免跨表查文章）", requiredMode = Schema.RequiredMode.REQUIRED, example = "5960")
    @ExcelProperty("冗余博客ID（避免跨表查文章）")
    private Long postId;

    @Schema(description = "回复者用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13414")
    @ExcelProperty("回复者用户ID")
    private Long userId;

    @Schema(description = "被回复的用户ID（可空，默认回复父级评论人）", example = "22617")
    @ExcelProperty("被回复的用户ID（可空，默认回复父级评论人）")
    private Long replyToUserId;

    @Schema(description = "回复内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("回复内容")
    private String content;

    @Schema(description = "状态：0-待审核 1-已发布 2-已删除", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态：0-待审核 1-已发布 2-已删除")
    private Integer status;

    @Schema(description = "审核人ID（管理员）", example = "27799")
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
