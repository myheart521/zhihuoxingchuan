package com.example.project.module.blog.controller.admin.postcategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.util.*;

@Schema(description = "管理后台 - 博客与分类关联新增/修改 Request VO")
@Data
public class PostCategorySaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27379")
    private Long id;

    @Schema(description = "博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29621")
    @NotNull(message = "博客ID不能为空")
    private Long postId;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3584")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

}
