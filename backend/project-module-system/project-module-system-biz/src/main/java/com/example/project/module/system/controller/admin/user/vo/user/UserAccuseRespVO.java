package com.example.project.module.system.controller.admin.user.vo.user;

import com.example.project.framework.common.enums.CommonStatusEnum;
import com.example.project.module.system.enums.common.SexEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 举报用户信息 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccuseRespVO {

    @Schema(description = "用户id", example = "666")
    private Long id;
    /**
     * 用户账号
     */
    @Schema(description = "用户账号", example = "admin")
    private String username;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "帅比")
    private String nickname;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱", example = "maintainer@example.invalid")
    private String email;
    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "00000000000")
    private String mobile;
    /**
     * 用户性别
     * <p>
     * 枚举类 {@link SexEnum}
     */
    @Schema(description = "用户性别")
    private Integer sex;
    /**
     * 用户头像
     */
    @Schema(description = "用户头像", example = "https://www.iocoder.cn/images/common/wechat_001.png")
    private String avatar;

    /**
     * 帐号状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    @Schema(description = "帐号状态")
    private Integer status;

    /**
     * 初始100分，违规扣分
     */
    @Schema(description = "初始100分，违规扣分")
    private Integer reputation;

    /**
     * 博客数量
     */
    @Schema(description = "博客数量")
    private Integer blogCount;

    /**
     * 点赞数量
     */
    @Schema(description = "点赞数量")
    private Long likeCount;

    /**
     * 邀请码
     */
    @Schema(description = "邀请码")
    private String inviteCode;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    private String realName;

    /**
     * 审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝
     */
    @Schema(description = "审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝")
    private Integer auditStatus;

    /**
     * 身份证前面
     */
    @Schema(description = "身份证前面")
    private String idCardFront;

    /**
     * 身份证背面
     */
    @Schema(description = "身份证背面")
    private String idCardBack;

    /**
     * 审核的备注（拒绝原因等）
     */
    @Schema(description = "审核的备注（拒绝原因等）")
    private String auditComment;

    /**
     * 是否请求审核 0-请求 1-不请求
     */
    @Schema(description = "是否请求审核 0-请求 1-不请求")
    private Integer application;
}
