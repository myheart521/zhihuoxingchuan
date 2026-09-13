package com.example.project.module.blog.controller.admin.category.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import java.util.*;

@Schema(description = "管理后台 - 博客分类新增/修改 Request VO")
@Data
public class CategorySaveReqVO {
    @Schema(description = "分类id", example = "666")
    private Long id;
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "分类名称不能为空")
    private String name;
    @Schema(description = "父分类ID", example = "28407")
    private Long parentId;
}
