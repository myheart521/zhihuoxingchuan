package com.example.project.module.blog.controller.admin.accuse.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.example.project.module.system.controller.admin.user.vo.user.UserAccuseRespVO;
import com.example.project.module.system.controller.admin.user.vo.user.UserRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客举报记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AccuseRespVO {

    @Schema(description = "举报ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3046")
    @ExcelProperty("举报ID")
    private Long id;

    @Schema(description = "被举报博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9310")
    @ExcelProperty("被举报博客ID")
    private Long postId;

    @Schema(description = "举报人ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14716")
    @ExcelProperty("举报人ID")
    private Long userId;

    @Schema(description = "举报类型：字典内容", example = "1")
    @ExcelProperty("举报类型：字典内容")
    private String type;

    @Schema(description = "举报详情")
    @ExcelProperty("举报详情")
    private String content;

    @Schema(description = "处理状态：0-待处理 1-已处理", example = "1")
    @ExcelProperty("处理状态：0-待处理 1-已处理")
    private Integer status;

    @Schema(description = "处理人ID（管理员）", example = "536")
    @ExcelProperty("处理人ID（管理员）")
    private Long handleUserId;

    @Schema(description = "处理人名字", example = "536")
    @ExcelProperty("处理人名字")
    private String handleUserName;

    @Schema(description = "处理人角色", example = "536")
    @ExcelProperty("处理人角色")
    private String handleUserRole;

    @Schema(description = "处理时间")
    @ExcelProperty("处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "举报时间")
    @ExcelProperty("举报时间")
    private LocalDateTime createTime;

    @Schema(description = "举报人详细信息")
    private UserAccuseRespVO accuseUser;

    @Schema(description = "举报人的角色")
    private String accuseRole;

    @Schema(description = "被举报博客的发布者的详细信息")
    private UserAccuseRespVO accusedUser;

    @Schema(description = "被举报人的角色")
    private String accusedRole;
}
