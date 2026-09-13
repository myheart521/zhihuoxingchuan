package com.example.project.module.blog.dal.dataobject.praise;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 博客点赞 DO
 *

 */
@TableName("blog_praise")
@KeySequence("blog_praise_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PraiseDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 博客id
     */
    private Long blogId;
    /**
     * 用户id
     */
    private Long userId;

}
