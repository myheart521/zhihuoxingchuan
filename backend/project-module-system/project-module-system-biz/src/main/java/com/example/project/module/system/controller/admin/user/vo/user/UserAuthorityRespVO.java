package com.example.project.module.system.controller.admin.user.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.project.framework.excel.core.annotations.DictFormat;
import com.example.project.framework.excel.core.convert.DictConvert;
import com.example.project.module.system.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "前台 - 用户审核分页 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorityRespVO {
    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("用户编号")
    private Long id;

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "project")
    @ExcelProperty("用户名称")
    private String username;

    @Schema(description = "手机号码", example = "00000000000")
    @ExcelProperty("手机号码")
    private String mobile;

    @Schema(description = "用户性别，参见 SexEnum 枚举类", example = "1")
    @ExcelProperty(value = "用户性别", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.USER_SEX)
    private Integer sex;

    @Schema(description = "用户头像", example = "https://www.iocoder.cn/xxx.png")
    private String avatar;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "帐号状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "信誉分", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer reputation;

    @Schema(description = "博客数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer blogCount;

    @Schema(description = "点赞数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer likeCount;

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED, example = "6666")
    private String inviteCode;

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "项目维护者")
    private String realName;

    @Schema(description = "审核状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer auditStatus;

    @Schema(description = "身份证正面", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.invalid/resource")
    private String idCardFront;

    @Schema(description = "身份证反面", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.invalid/resource")
    private String idCardBack;

    @Schema(description = "审核人员的留言", requiredMode = Schema.RequiredMode.REQUIRED, example = "非法信息")
    private String auditComment;

    /**
     * 申请管理员的介绍
     */
    @Schema(description = "申请管理员的介绍", requiredMode = Schema.RequiredMode.REQUIRED, example = "介绍")
    private String applicationIntroduction;

    /**
     * 申请管理员的理由
     */
    @Schema(description = "申请管理员的理由", requiredMode = Schema.RequiredMode.REQUIRED, example = "理由")
    private String ReasonApplication;


}
