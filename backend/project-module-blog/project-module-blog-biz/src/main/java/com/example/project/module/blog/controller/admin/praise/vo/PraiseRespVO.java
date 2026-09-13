package com.example.project.module.blog.controller.admin.praise.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客点赞 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PraiseRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "867")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "博客id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17659")
    @ExcelProperty("博客id")
    private Long blogId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25265")
    @ExcelProperty("用户id")
    private Long userId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
