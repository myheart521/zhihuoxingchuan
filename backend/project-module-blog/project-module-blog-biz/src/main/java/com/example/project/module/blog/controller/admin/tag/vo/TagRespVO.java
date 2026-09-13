package com.example.project.module.blog.controller.admin.tag.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 博客标签 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TagRespVO {

    @Schema(description = "标签ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18759")
    @ExcelProperty("标签ID")
    private Long id;

    @Schema(description = "标签名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("标签名称")
    private String name;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
