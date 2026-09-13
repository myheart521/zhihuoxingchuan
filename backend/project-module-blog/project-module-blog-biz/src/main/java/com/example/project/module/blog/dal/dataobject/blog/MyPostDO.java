package com.example.project.module.blog.dal.dataobject.blog;

import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;

/**
 * 博客表（含审核、置顶状态） DO
 *

 */
@TableName("blog_post")
@KeySequence("blog_post_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPostDO extends BaseDO {

    /**
     * 博客ID
     */
    @TableId
    private Long id;
    /**
     * 作者ID
     */
    private Long userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    /**
     * 摘要
     */
    private String summary;
    /**
     * 状态
     *
     */
    private Integer status;
    /**
     * 是否置顶
     *
     */
    private Integer isTop;
    /**
     * 置顶时间
     */
    private LocalDateTime topTime;
    /**
     * 可见性
     *
     */
    private Integer visibility;
    /**
     * 阅读量
     */
    private Integer viewCount;
    /**
     * 点赞数
     */
    private Integer likeCount;
    /**
     * 审核人ID（管理员）
     */
    private Long auditUserId;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    /**
     * 审核意见
     */
    private String auditComment;
    /**
     * 博客封面
     */
    private String frontCover;

    /**
     * 是否是实践主题博客,默认为0
     */
    private Integer isPractice;

}
