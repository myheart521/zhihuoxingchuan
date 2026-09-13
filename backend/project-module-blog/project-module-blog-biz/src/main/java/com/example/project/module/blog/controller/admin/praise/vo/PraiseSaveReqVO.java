package com.example.project.module.blog.controller.admin.praise.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 博客点赞新增/修改 Request VO")
@Data
public class PraiseSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "867")
    private Long id;

    @Schema(description = "博客id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17659")
    @NotNull(message = "博客id不能为空")
    private Long blogId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25265")
    @NotNull(message = "用户id不能为空")
    private Long userId;

}
