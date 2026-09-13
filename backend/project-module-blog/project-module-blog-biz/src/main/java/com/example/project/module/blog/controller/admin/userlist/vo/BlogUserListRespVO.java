package com.example.project.module.blog.controller.admin.userlist.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.example.project.framework.excel.core.annotations.DictFormat;
import com.example.project.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户表（含角色、审核、信誉等信息） Response VO")
@Data
@ExcelIgnoreUnannotated
public class BlogUserListRespVO {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11482")
    @ExcelProperty("用户ID")
    private Long id;

    @Schema(description = "用户名（唯一）", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("用户名（唯一）")
    private String username;

    @Schema(description = "邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @Schema(description = "角色：0-未审核用户 1-普通用户 2-管理员用户", example = "0")
    @ExcelProperty(value = "角色：0-未审核用户 1-普通用户 2-管理员用户", converter = DictConvert.class)
    @DictFormat("blog_user_authority") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer role;

    @Schema(description = "账号是否被禁用：0-正常 1-禁用", example = "0")
    @ExcelProperty(value = "账号是否被禁用：0-正常 1-禁用", converter = DictConvert.class)
    @DictFormat("blog_user_lock") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer isLocked;

    @Schema(description = "信誉等级（初始100分，违规扣分）")
    @ExcelProperty("信誉等级（初始100分，违规扣分）")
    private Integer reputation;

    @Schema(description = "已发布博客数量（用于管理员申请条件）", example = "30549")
    @ExcelProperty("已发布博客数量（用于管理员申请条件）")
    private Integer blogCount;

    @Schema(description = "博客总获赞数（用于管理员申请条件）", example = "7007")
    @ExcelProperty("博客总获赞数（用于管理员申请条件）")
    private Integer likeCount;

    @Schema(description = "成功使用的邀请码（管理员激活用）")
    @ExcelProperty("成功使用的邀请码（管理员激活用）")
    private String inviteCode;

    @Schema(description = "真实姓名（证件照审核用）", example = "赵六")
    @ExcelProperty("真实姓名（证件照审核用）")
    private String realName;

    @Schema(description = "身份证正面MinIO路径")
    @ExcelProperty("身份证正面MinIO路径")
    private String idCardFront;

    @Schema(description = "身份证背面MinIO路径")
    @ExcelProperty("身份证背面MinIO路径")
    private String idCardBack;

    @Schema(description = "审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝", example = "2")
    @ExcelProperty(value = "审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝", converter = DictConvert.class)
    @DictFormat("blog_user_stage") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer auditStatus;

    @Schema(description = "审核备注（拒绝原因等）")
    @ExcelProperty("审核备注（拒绝原因等）")
    private String auditComment;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
