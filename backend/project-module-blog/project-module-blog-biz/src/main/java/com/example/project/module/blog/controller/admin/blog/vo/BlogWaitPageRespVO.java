package com.example.project.module.blog.controller.admin.blog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "前台 - 博客详情 Response VO")
@Data
public class BlogWaitPageRespVO {
    @Schema(description = "审核意见", example = "审核通过")
    private String auditComment;

    @Schema(description = "审核时间", example = "2022-01-01 00:00:00")
    private LocalDateTime auditTime;

    @Schema(description = "创建时间", example = "2022-01-01 00:00:00")
    private LocalDateTime createTime;

    @Schema(description = "审核人姓名", example = "2022-01-01 00:00:00")
    private String auditUserName;

    @Schema(description = "博客状态 0-草稿 1-待审核 2-已发布 3-下架", example = "0")
    private Integer status;

}
