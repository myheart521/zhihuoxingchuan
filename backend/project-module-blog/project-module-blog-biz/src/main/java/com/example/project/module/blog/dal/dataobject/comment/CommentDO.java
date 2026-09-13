package com.example.project.module.blog.dal.dataobject.comment;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 顶级评论 DO
 *

 */
@TableName("blog_comment")
@KeySequence("blog_comment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDO extends BaseDO {

    /**
     * 评论ID（顶级）
     */
    @TableId
    private Long id;
    /**
     * 关联的博客ID
     */
    private Long postId;
    /**
     * 评论者用户ID
     */
    private Long userId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 状态：0-待审核 1-已发布 2-已删除
     */
    private Integer status;
    /**
     * 审核人ID（管理员）
     */
    private Long auditUserId;

}
