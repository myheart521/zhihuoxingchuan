package com.example.project.module.blog.controller.admin.blog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;



import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客表（含审核、置顶状态）新增/修改 Request VO")
@Data
public class PostSaveReqVO {

    @Schema(description = "博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13890")
    private Long id;

    @Schema(description = "作者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20094")
    @NotNull(message = "作者ID不能为空")
    private Long userId;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "内容不能为空")
    private String content;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "置顶时间")
    private LocalDateTime topTime;

    @Schema(description = "可见性")
    private Integer visibility;

    @Schema(description = "阅读量", example = "31102")
    private Integer viewCount;

    @Schema(description = "点赞数", example = "29409")
    private Integer likeCount;

    @Schema(description = "审核人ID（管理员）", example = "16079")
    private Long auditUserId;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "审核意见")
    private String auditComment;

    @Schema(description = "博客封面")
    private String frontCover;

}
