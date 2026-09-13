package com.example.project.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Schema(description = "管理后台 - 更新管理员权限或者赋予管理员权限 Request VO")
@Data
public class UserUpdateManageReqVO {
    @Schema(description = "需要更新的角色权限字符", requiredMode = Schema.RequiredMode.REQUIRED, example = "blog_admin")
    @NotNull(message = "需要更新的角色权限字符不能为空")
    private String newCode;

    @Schema(description = "原来的权限字符", requiredMode = Schema.RequiredMode.REQUIRED, example = "blog_examine")
    @NotNull(message = "原来的权限字符不能为空")
    private String oldCode;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "666")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "密码", example = "123456")
    private String password;
}
