package com.example.project.module.blog.dal.dataobject.comment;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 二级评论回复 DO
 *

 */
@TableName("blog_comment_reply")
@KeySequence("blog_comment_reply_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReplyDO extends BaseDO {

    /**
     * 回复ID（二级）
     */
    @TableId
    private Long id;
    /**
     * 关联的顶级评论ID
     */
    private Long parentId;
    /**
     * 冗余博客ID（避免跨表查文章）
     */
    private Long postId;
    /**
     * 回复者用户ID
     */
    private Long userId;
    /**
     * 被回复的用户ID（可空，默认回复父级评论人）
     */
    private Long replyToUserId;
    /**
     * 回复内容
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
