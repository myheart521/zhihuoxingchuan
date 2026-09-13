package com.example.project.module.blog.dal.dataobject.accuse;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 博客举报记录 DO
 *

 */
@TableName("blog_accuse")
@KeySequence("blog_accuse_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccuseDO extends BaseDO {

    /**
     * 举报ID
     */
    @TableId
    private Long id;
    /**
     * 被举报博客ID
     */
    private Long postId;
    /**
     * 举报人ID
     */
    private Long userId;
    /**
     * 举报类型：字典内容
     */
    private String type;
    /**
     * 举报详情
     */
    private String content;
    /**
     * 处理状态：0-待处理 1-已处理
     */
    private Integer status;
    /**
     * 处理人ID（管理员）
     */
    private Long handleUserId;
    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理方式
     */
    private Integer processWay;

}
