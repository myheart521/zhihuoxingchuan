package com.example.project.module.blog.controller.admin.accuse.vo;

import com.example.project.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.example.project.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 博客举报记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccusePageReqVO extends PageParam {

    @Schema(description = "被举报博客ID", example = "9310")
    private Long postId;

    @Schema(description = "举报人ID", example = "14716")
    private Long userId;

    @Schema(description = "举报类型：字典内容", example = "1")
    private String type;

    @Schema(description = "举报详情")
    private String content;

    @Schema(description = "处理状态：0-待处理 1-已处理", example = "1")
    private Integer status;

    @Schema(description = "处理人ID（管理员）", example = "536")
    private Long handleUserId;

    @Schema(description = "处理时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] handleTime;

    @Schema(description = "举报时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
