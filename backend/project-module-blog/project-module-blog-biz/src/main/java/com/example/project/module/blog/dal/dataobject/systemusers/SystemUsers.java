package com.example.project.module.blog.dal.dataobject.systemusers;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户信息表
 *
 * @TableName system_users
 */
@TableName(value = "system_users")
@Data
public class SystemUsers implements Serializable {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 密码
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
     * 部门ID
     */
    private Long deptId;

    /**
     * 岗位编号数组
     */
    private String postIds;

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
     */
    private Integer sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 帐号状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 租户编号
     */
    private Long tenantId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
