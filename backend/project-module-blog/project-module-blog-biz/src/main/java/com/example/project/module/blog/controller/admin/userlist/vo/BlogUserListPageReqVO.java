package com.example.project.module.blog.controller.admin.userlist.vo;

import com.example.project.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.example.project.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户表（含角色、审核、信誉等信息）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BlogUserListPageReqVO extends PageParam {

    @Schema(description = "用户名（唯一）", example = "张三")
    private String username;

    @Schema(description = "加密密码")
    private String password;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "角色：0-未审核用户 1-普通用户 2-管理员用户", example = "0")
    private Integer role;

    @Schema(description = "账号是否被禁用：0-正常 1-禁用", example = "0")
    private Integer isLocked;

    @Schema(description = "信誉等级（初始100分，违规扣分）")
    private Integer reputation;

    @Schema(description = "已发布博客数量（用于管理员申请条件）", example = "30549")
    private Integer blogCount;

    @Schema(description = "博客总获赞数（用于管理员申请条件）", example = "7007")
    private Integer likeCount;

    @Schema(description = "成功使用的邀请码（管理员激活用）")
    private String inviteCode;

    @Schema(description = "真实姓名（证件照审核用）", example = "赵六")
    private String realName;

    @Schema(description = "身份证正面MinIO路径")
    private String idCardFront;

    @Schema(description = "身份证背面MinIO路径")
    private String idCardBack;

    @Schema(description = "审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝", example = "2")
    private Integer auditStatus;

    @Schema(description = "审核备注（拒绝原因等）")
    private String auditComment;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
