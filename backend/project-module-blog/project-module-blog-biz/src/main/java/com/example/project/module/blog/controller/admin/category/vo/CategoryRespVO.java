package com.example.project.module.blog.controller.admin.category.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 博客分类 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CategoryRespVO {
    @Schema(description = "分类id", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    @ExcelProperty("分类id")
    private Long id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("分类名称")
    private String name;

    @Schema(description = "父分类ID", example = "28407")
    @ExcelProperty("父分类ID")
    private Long parentId;

}
