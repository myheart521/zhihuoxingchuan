package com.example.project.module.blog.controller.admin.blog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Schema(description = "前台 - 博客详情 Response VO")
@Data
public class BlogDetailRespVO {
    @Schema(description = "博客ID", example = "1")
    private Long id;
    @Schema(description = "标题", example = "示例博客标题")
    private String title;
    @Schema(description = "内容", example = "博客详细内容...")
    private String content;
    @Schema(description = "摘要", example = "博客摘要")
    private String summary;
    @Schema(description = "封面图片", example = "https://example.invalid/resource")
    private String frontCover;
    @Schema(description = "状态", example = "1")
    private Integer status;
    @Schema(description = "是否置顶", example = "0")
    private Integer isTop;
    @Schema(description = "置顶时间")
    private Date topTime;
    @Schema(description = "可见性", example = "0")
    private Integer visibility;
    @Schema(description = "阅读量", example = "100")
    private Integer viewCount;
    @Schema(description = "点赞数", example = "50")
    private Integer likeCount;
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

    @Schema(description = "是否为实践主题")
    private Integer isPractice;

    @Data
    @Schema(description = "作者信息")
    public static class Author {
        @Schema(description = "作者ID", example = "1")
        private Long id;

        @Schema(description = "用户名", example = "admin")
        private String username;

        @Schema(description = "角色", example = "admin")
        private String role;
    }

    @Data
    @Schema(description = "分类信息")
    public static class Category {
        @Schema(description = "分类ID", example = "1")
        private Long id;

        @Schema(description = "分类名称", example = "技术")
        private String name;
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
