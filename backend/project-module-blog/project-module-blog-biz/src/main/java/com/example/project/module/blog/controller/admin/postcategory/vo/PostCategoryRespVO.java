package com.example.project.module.blog.controller.admin.postcategory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 博客与分类关联 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PostCategoryRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27379")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29621")
    @ExcelProperty("博客ID")
    private Long postId;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3584")
    @ExcelProperty("分类ID")
    private Long categoryId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
