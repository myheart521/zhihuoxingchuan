package com.example.project.module.blog.controller.admin.comment.vo;

import com.example.project.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.example.project.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 顶级评论分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommentPageReqVO extends PageParam {

    @Schema(description = "关联的博客ID", example = "12650")
    private Long postId;

    @Schema(description = "评论者用户ID", example = "22465")
    private Long userId;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "状态：0-待审核 1-已发布 2-已删除", example = "2")
    private Integer status;

    @Schema(description = "审核人ID（管理员）", example = "3888")
    private Long auditUserId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
