package com.example.project.module.blog.controller.admin.userrole.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 用户和角色关联 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UserRoleRespVO {

    @Schema(description = "自增编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19065")
    @ExcelProperty("自增编号")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4874")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17574")
    @ExcelProperty("角色ID")
    private Long roleId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
