package com.example.project.module.blog.controller.admin.blog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "前台 - 博客新增响应 Response VO")
@Data
public class BlogCreateRespVO {
    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "标题", example = "666")
    private String title;

    @Schema(description = "状态", example = "0")
    private Integer status;
}
