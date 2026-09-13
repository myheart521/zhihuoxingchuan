package com.example.project.module.blog.dal.dataobject.userlist;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 用户表（含角色、审核、信誉等信息） DO
 *

 */
@TableName("blog_sys_user")
@KeySequence("blog_sys_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserListDO extends BaseDO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 用户名（唯一）
     */
    private String username;
    /**
     * 加密密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 角色：0-未审核用户 1-普通用户 2-管理员用户
     *
     */
    private Integer role;
    /**
     * 账号是否被禁用：0-正常 1-禁用
     *
     */
    private Integer isLocked;
    /**
     * 信誉等级（初始100分，违规扣分）
     */
    private Integer reputation;
    /**
     * 已发布博客数量（用于管理员申请条件）
     */
    private Integer blogCount;
    /**
     * 博客总获赞数（用于管理员申请条件）
     */
    private Integer likeCount;
    /**
     * 成功使用的邀请码（管理员激活用）
     */
    private String inviteCode;
    /**
     * 真实姓名（证件照审核用）
     */
    private String realName;
    /**
     * 身份证正面MinIO路径
     */
    private String idCardFront;
    /**
     * 身份证背面MinIO路径
     */
    private String idCardBack;
    /**
     * 审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝
     *
     */
    private Integer auditStatus;
    /**
     * 审核备注（拒绝原因等）
     */
    private String auditComment;

}
