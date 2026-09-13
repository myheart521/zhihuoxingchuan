package com.example.project.module.blog.controller.admin.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.*;

@Schema(description = "管理后台 - 角色信息新增/修改 Request VO")
@Data
public class RoleSaveReqVO {

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24608")
    private Long id;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    @Schema(description = "角色权限字符串", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "角色权限字符串不能为空")
    private String code;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）不能为空")
    private Integer dataScope;

    @Schema(description = "数据范围(指定部门数组)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "数据范围(指定部门数组)不能为空")
    private String dataScopeDeptIds;

    @Schema(description = "角色状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "角色状态（0正常 1停用）不能为空")
    private Integer status;

    @Schema(description = "角色类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "角色类型不能为空")
    private Integer type;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
