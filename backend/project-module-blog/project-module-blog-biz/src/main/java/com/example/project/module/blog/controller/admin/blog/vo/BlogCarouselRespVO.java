package com.example.project.module.blog.controller.admin.blog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "前台 - 实践主题的轮播图 Request VO")
@Data
public class BlogCarouselRespVO {
    @Schema(description = "博客ID", example = "1")
    private Long id;

    @Schema(description = "标题", example = "示例博客标题")
    private String title;

    @Schema(description = "摘要", example = "博客摘要")
    private String summary;

    @Schema(description = "封面图片", example = "https://example.invalid/resource")
    private String frontCover;
}
