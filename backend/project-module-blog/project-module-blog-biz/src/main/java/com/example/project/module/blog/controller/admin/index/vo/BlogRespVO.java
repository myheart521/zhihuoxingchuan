package com.example.project.module.blog.controller.admin.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "前台 - 博客列表 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BlogRespVO {
    @Schema(description = "博客ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "作者ID")
    private Long userId;

    @Schema(description = "作者名称")
    private String author;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "封面图片")
    private String frontCover;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "置顶时间")
    private Date topTime;

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
    private CategoryInfo category;

    @Schema(description = "标签列表")
    private List<TagInfo> tags;

    @Data
    @Schema(description = "分类信息")
    public static class CategoryInfo {
        @Schema(description = "分类ID")
        private Long id;

        @Schema(description = "分类名称")
        private String name;
    }

    @Data
    @Schema(description = "标签信息")
    public static class TagInfo {
        @Schema(description = "标签ID")
        private Long id;

        @Schema(description = "标签名称")
        private String name;
    }
}
