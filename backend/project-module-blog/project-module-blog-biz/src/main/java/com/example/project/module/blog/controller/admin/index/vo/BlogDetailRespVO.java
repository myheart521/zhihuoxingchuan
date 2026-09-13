package com.example.project.module.blog.controller.admin.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

@Schema(description = "前台 - 博客详情 Response VO")
@Data
public class BlogDetailRespVO {
    @Schema(description = "博客ID")
    private Long id;

    @Schema(description = "作者ID")
    private Long userId;

    @Schema(description = "作者名称")
    private String author;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容（HTML格式）")
    private String content;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "可见性")
    private Integer visibility;

    @Schema(description = "阅读量")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "分类信息")
    private BlogRespVO.CategoryInfo category;

    @Schema(description = "标签列表")
    private List<BlogRespVO.TagInfo> tags;

    @Schema(description = "作者信息")
    private AuthorInfo authorInfo;

    @Schema(description = "相关推荐")
    private List<RelatedPost> relatedPosts;

    @Data
    @Schema(description = "作者信息")
    public static class AuthorInfo {
        @Schema(description = "头像")
        private String avatar;

        @Schema(description = "角色")
        private Integer role;

        @Schema(description = "博客数")
        private Integer blogCount;

        @Schema(description = "获赞数")
        private Integer likeCount;
    }

    @Data
    @Schema(description = "相关推荐")
    public static class RelatedPost {
        @Schema(description = "博客ID")
        private Long id;

        @Schema(description = "标题")
        private String title;

        @Schema(description = "阅读量")
        private Integer viewCount;
    }
}
