package com.example.project.module.system.controller.admin.user.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.example.project.framework.excel.core.annotations.DictFormat;
import com.example.project.framework.excel.core.convert.DictConvert;
import com.example.project.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UserManageRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("用户编号")
    private Long id;

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "project")
    @ExcelProperty("用户名称")
    private String username;

    @Schema(description = "用户邮箱", example = "maintainer@example.invalid")
    @ExcelProperty("用户邮箱")
    private String email;

    @Schema(description = "角色编码", example = "[blog_unexamine, blog_examine]")
    private String code;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "帐号状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "信誉等级", example = "100")
    private Integer reputation;

    @Schema(description = "已发布博客数量", example = "10")
    private Integer blogCount;

    @Schema(description = "博客总获赞数", example = "100")
    private Integer likeCount;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "身份证正面", example = "https://example.invalid/resource")
    private String idCardFront;

    @Schema(description = "身份证背面", example = "https://example.invalid/resource")
    private String idCardBack;

    @Schema(description = "审核状态", example = "1")
    private Integer auditStatus;

    @Schema(description = "审核备注", example = "审核通过")
    private String auditComment;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;

    @Schema(description = "是否申请管理员0-申请 1-未申请", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer application;
}
