package com.example.project.module.blog.controller.admin.accuse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客举报记录新增/修改 Request VO")
@Data
public class AccuseSaveReqVO {

    @Schema(description = "举报的主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3046")
    private Long id;

    @Schema(description = "处理方式-字典值0-为违规驳回 1-违规下架 2-违规删除,只有为1或2时才可以：修改角色、修改用户账号状态、修改博客状态、修改用户的积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "3046")
    @NotNull(message = "处理方式不能为空")
    private int processWay;

    @Schema(description = "被举报博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9310")
    @NotNull(message = "被举报博客ID不能为空")
    private Long postId;

    @Schema(description = "被举报用户的id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9310")
    private Long accusedUserId;

    /**
     * 举报类型：字典内容
     */
    @Schema(description = "举报类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "违规")
    private String type;
    /**
     * 举报详情
     */
    @Schema(description = "举报详情", requiredMode = Schema.RequiredMode.REQUIRED, example = "违规")
    private String content;

    @Schema(description = "举报人ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14716")
    @NotNull(message = "举报人ID不能为空")
    private Long accuseUserId;

    @Schema(description = "将要修改的用户角色 blog_examine blog_unexamine", requiredMode = Schema.RequiredMode.REQUIRED, example = "blog_examine")
    private String roleCode;

    @Schema(description = "是否禁用被举报人的账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean isBlock;

    @Schema(description = "将要修改的博客状态0-草稿 1-待审核 2-已发布 3-下架", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private int blogStatus;

    @Schema(description = "要扣多少分", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private int score;
}
