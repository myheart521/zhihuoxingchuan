package com.example.project.module.blog.dal.dataobject.carousel;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.example.project.framework.mybatis.core.dataobject.BaseDO;

/**
 * 轮播图 DO
 *

 */
@TableName("blog_carousel")
@KeySequence("blog_carousel_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarouselDO extends BaseDO {

    /**
     * 轮播图ID
     */
    @TableId
    private Long id;
    /**
     * 图片URL
     */
    private String image;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 链接地址
     */
    private String link;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 状态：0-禁用 1-启用
     *
     */
    private Integer status;

}
