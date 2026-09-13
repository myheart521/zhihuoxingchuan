package com.example.project.module.blog.dal.dataobject.category;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;

/**
 * 博客分类 DO
 *

 */
@TableName("blog_category")
@KeySequence("blog_category_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDO extends BaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 分类ID
     */
    @TableId
    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 父分类ID
     */
    private Long parentId;



}
