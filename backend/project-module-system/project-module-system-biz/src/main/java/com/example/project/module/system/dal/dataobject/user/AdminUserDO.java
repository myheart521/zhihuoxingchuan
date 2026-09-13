package com.example.project.module.system.dal.dataobject.user;

import com.example.project.framework.common.enums.CommonStatusEnum;
import com.example.project.framework.tenant.core.db.TenantBaseDO;
import com.example.project.module.system.enums.common.SexEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 管理后台的用户 DO
 *

 */
@TableName(value = "system_users", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@KeySequence("system_users_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDO extends TenantBaseDO {


    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 加密后的密码
     *
     * 因为目前使用 {@link BCryptPasswordEncoder} 加密器，所以无需自己处理 salt 盐
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 备注
     */
    private String remark;
    /**
     * 部门 ID
     */
    private Long deptId;
    /**
     * 岗位编号数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> postIds;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户性别
     *
     * 枚举类 {@link SexEnum}
     */
    private Integer sex;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 初始100分，违规扣分
     */
    private Integer reputation;

    /**
     * 博客数量
     */
    private Integer blogCount;

    /**
     * 点赞数量
     */
    private Long likeCount;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝
     */
    private Integer auditStatus;

    /**
     * 身份证前面
     */
    private String idCardFront;

    /**
     * 身份证背面
     */
    private String idCardBack;

    /**
     * 审核的备注（拒绝原因等）
     */
    private String auditComment;

    /**
     * 是否请求审核 0-请求 1-不请求
     */
    private Integer application;

    /**
     * 申请管理员的介绍
     */
    private String applicationIntroduction;

    /**
     * 申请管理员的理由
     */
    private String ReasonApplication;

}
