package com.example.project.module.blog.dal.dataobject.tag;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;

/**
 * 博客标签 DO
 *

 */
@TableName("blog_tag")
@KeySequence("blog_tag_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDO extends BaseDO {

    /**
     * 标签ID
     */
    @TableId
    private Long id;
    /**
     * 标签名称
     */
    private String name;

}
