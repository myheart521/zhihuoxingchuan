package com.example.project.module.blog.controller.admin.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 角色信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RoleRespVO {

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24608")
    @ExcelProperty("角色ID")
    private Long id;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("角色名称")
    private String name;

    @Schema(description = "角色权限字符串", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("角色权限字符串")
    private String code;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("显示顺序")
    private Integer sort;

    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    private Integer dataScope;

    @Schema(description = "数据范围(指定部门数组)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数据范围(指定部门数组)")
    private String dataScopeDeptIds;

    @Schema(description = "角色状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("角色状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "角色类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("角色类型")
    private Integer type;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
