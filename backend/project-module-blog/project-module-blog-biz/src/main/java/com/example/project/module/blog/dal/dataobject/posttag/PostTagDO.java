package com.example.project.module.blog.dal.dataobject.posttag;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;

/**
 * 博客标签关联 DO
 *

 */
@TableName("blog_post_tag")
@KeySequence("blog_post_tag_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostTagDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 博客ID
     */
    private Long postId;
    /**
     * 标签ID
     */
    private Long tagId;

}
