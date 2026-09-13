package com.example.project.module.blog.controller.admin.blog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Schema(description = "前台 - 博客详情 Response VO")
@Data
public class BlogDetailByIdRespVO {
    @Schema(description = "博客ID", example = "1")
    private Long id;

    @Schema(description = "作者ID", example = "1")
    private Long userId;

    @Schema(description = "标题", example = "示例博客标题")
    private String title;

    @Schema(description = "内容（HTML格式）", example = "<p>博客详细内容...</p>")
    private String content;

    @Schema(description = "摘要", example = "博客摘要")
    private String summary;

    @Schema(description = "封面图片", example = "https://example.invalid/resource")
    private String frontCover;

    @Schema(description = "状态：0-草稿 1-待审核 2-已发布 3-下架", example = "1")
    private Integer status;

    @Schema(description = "是否置顶：0-否 1-是", example = "0")
    private Integer isTop;

    @Schema(description = "置顶时间")
    private Date topTime;

    @Schema(description = "可见性：0-公开 1-仅自己 2-粉丝可见", example = "0")
    private Integer visibility;

    @Schema(description = "阅读量", example = "100")
    private Integer viewCount;

    @Schema(description = "点赞数", example = "50")
    private Integer likeCount;

    @Schema(description = "审核人ID", example = "1")
    private Long auditUserId;

    @Schema(description = "审核时间")
    private Date auditTime;

    @Schema(description = "审核意见")
    private String auditComment;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "作者信息")
    private Author author;

    @Schema(description = "分类信息")
    private Category category;

    @Schema(description = "标签列表")
    private List<Tag> tags;

    @Schema(description = "是否点赞")
    private boolean praise;

    @Data
    @Schema(description = "作者信息")
    public static class Author {
        @Schema(description = "用户ID", example = "1")
        private Long id;

        @Schema(description = "用户名", example = "admin")
        private String username;

        @Schema(description = "角色名称", example = "普通用户")
        private List<String> name;

        @Schema(description = "角色编码", example = "普通用户")
        private List<String> code;

        @Schema(description = "信誉等级", example = "100")
        private Integer reputation;

        @Schema(description = "博客数量", example = "10")
        private Integer blogCount;

        @Schema(description = "获赞数", example = "100")
        private Integer likeCount;
    }

    @Data
    @Schema(description = "分类信息")
    public static class Category {
        @Schema(description = "分类ID", example = "1")
        private Long id;

        @Schema(description = "分类名称", example = "技术")
        private String name;

        @Schema(description = "父分类ID", example = "0")
        private Long parentId;
    }

    @Data
    @Schema(description = "标签信息")
    public static class Tag {
        @Schema(description = "标签ID", example = "1")
        private Long id;

        @Schema(description = "标签名称", example = "Java")
        private String name;
    }
}
