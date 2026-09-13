package com.example.project.module.blog.controller.admin.userrole.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.util.*;

@Schema(description = "管理后台 - 用户和角色关联新增/修改 Request VO")
@Data
public class UserRoleSaveReqVO {

    @Schema(description = "自增编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19065")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4874")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17574")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

}
