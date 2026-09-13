package com.example.project.module.blog.controller.admin.posttag.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 博客标签关联 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PostTagRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5205")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14756")
    @ExcelProperty("博客ID")
    private Long postId;

    @Schema(description = "标签ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19322")
    @ExcelProperty("标签ID")
    private Long tagId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
