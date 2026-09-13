package com.example.project.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(description = "管理后台 - 用户分页 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserApplyManageReqVO {
    @Schema(description = "个人介绍", example = "帅比")
    @NotNull(message = "个人介绍不能为空")
    private String applicationIntroduction;

    @Schema(description = "申请理由", example = "申请理由")
    @NotNull(message = "申请理由不能为空")
    private String reasonApplication;
}
