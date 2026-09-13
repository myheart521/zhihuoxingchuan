package com.example.project.module.blog.controller.admin.blog.vo;

import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.project.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.example.project.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 博客表（含审核、置顶状态）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PostPageReqVO extends PageParam {

    @Schema(description = "作者ID", example = "20094")
    private Long userId;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "可见性")
    private Integer visibility;
    // 新增字段
    @Schema(description = "搜索关键词（标题）")
    private String keyword;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "标签ID")
    private Long tagId;

    @Schema(description = "创建时间范围-开始")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "创建时间范围-结束")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;


    public String toCacheKey() {
        return String.format("%d_%d_%s_%s_%s_%s_%s_%s_%s_%s",
                this.getPageNo(),
                this.getPageSize(),
                safeValue(this.status),
                safeValue(this.isTop),
                safeValue(this.visibility),
                safeString(this.keyword),
                safeValue(this.categoryId),
                safeValue(this.tagId),
                safeDate(this.startTime),
                safeDate(this.endTime)
        );
    }

    private Object safeValue(Object value) {
        return value != null ? value : "NULL";
    }

    private String safeString(String value) {
        return StringUtils.hasText(value) ? value : "NULL";
    }

    private String safeDate(LocalDateTime date) {
        return date != null ?
                date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) :
                "NULL";
    }

}
