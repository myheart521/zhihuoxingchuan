package com.example.project.module.blog.controller.admin.comment.vo;

import com.example.project.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.example.project.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 二级评论回复分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommentReplyPageReqVO extends PageParam {

    @Schema(description = "关联的顶级评论ID", example = "23647")
    private Long parentId;

    @Schema(description = "冗余博客ID（避免跨表查文章）", example = "5960")
    private Long postId;

    @Schema(description = "回复者用户ID", example = "13414")
    private Long userId;

    @Schema(description = "被回复的用户ID（可空，默认回复父级评论人）", example = "22617")
    private Long replyToUserId;

    @Schema(description = "回复内容")
    private String content;

    @Schema(description = "状态：0-待审核 1-已发布 2-已删除", example = "1")
    private Integer status;

    @Schema(description = "审核人ID（管理员）", example = "27799")
    private Long auditUserId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
