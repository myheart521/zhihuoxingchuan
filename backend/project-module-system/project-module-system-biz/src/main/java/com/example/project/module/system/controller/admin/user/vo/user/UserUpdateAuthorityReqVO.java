package com.example.project.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Schema(description = "管理后台 - 用户更新审核状态 Request VO")
@Data
public class UserUpdateAuthorityReqVO {
    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "角色编号不能为空")
    private Long id;

    @Schema(description = "审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer auditStatus;

    @Schema(description = "审核意见", example = "包含敏感信息")
    private String auditComment;
}
