package com.example.project.module.system.controller.admin.user.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Schema(description = "管理后台 - 更新管理员权限或者赋予管理员权限 Request VO")
@Data
public class UserUpdateInfoReqVO {
    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "帅比")
    @NotNull(message = "真实姓名不能为空")
    private String realName;

    /**
     * 身份证前面
     */
    @Schema(description = "身份证前面", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://www.iocoder.cn/xxx.png")
    @NotNull(message = "身份证前面不能为空")
    private String idCardFront;

    /**
     * 身份证背面
     */
    @Schema(description = "身份证背面", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://www.iocoder.cn/xxx.png")
    @NotNull(message = "身份证背面不能为空")
    private String idCardBack;

}
