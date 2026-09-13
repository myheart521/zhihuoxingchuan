package com.example.project.module.blog.controller.admin.posttag.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.util.*;

@Schema(description = "管理后台 - 博客标签关联新增/修改 Request VO")
@Data
public class PostTagSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5205")
    private Long id;

    @Schema(description = "博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14756")
    @NotNull(message = "博客ID不能为空")
    private Long postId;

    @Schema(description = "标签ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19322")
    @NotNull(message = "标签ID不能为空")
    private Long tagId;

}
