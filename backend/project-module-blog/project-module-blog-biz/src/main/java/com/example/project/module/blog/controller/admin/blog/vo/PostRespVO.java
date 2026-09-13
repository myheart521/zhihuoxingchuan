package com.example.project.module.blog.controller.admin.blog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import com.example.project.framework.excel.core.annotations.DictFormat;
import com.example.project.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 博客表（含审核、置顶状态） Response VO")
@Data
@ExcelIgnoreUnannotated
public class PostRespVO {

    @Schema(description = "博客ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13890")
    @ExcelProperty("博客ID")
    private Long id;

    @Schema(description = "作者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20094")
    @ExcelProperty("作者ID")
    private Long userId;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标题")
    private String title;

    @Schema(description = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("内容")
    private String content;

    @Schema(description = "状态", example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("blog_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "是否置顶")
    @ExcelProperty(value = "是否置顶", converter = DictConvert.class)
    @DictFormat("blog_is_top") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer isTop;

    @Schema(description = "置顶时间")
    @ExcelProperty("置顶时间")
    private LocalDateTime topTime;

    @Schema(description = "可见性")
    @ExcelProperty(value = "可见性", converter = DictConvert.class)
    @DictFormat("blog_is_public") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer visibility;

    @Schema(description = "阅读量", example = "31102")
    @ExcelProperty("阅读量")
    private Integer viewCount;

    @Schema(description = "点赞数", example = "29409")
    @ExcelProperty("点赞数")
    private Integer likeCount;

    @Schema(description = "审核人ID（管理员）", example = "16079")
    @ExcelProperty("审核人ID（管理员）")
    private Long auditUserId;

    @Schema(description = "审核时间")
    @ExcelProperty("审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "审核意见")
    @ExcelProperty("审核意见")
    private String auditComment;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "博客封面")
    @ExcelProperty("博客封面")
    private String frontCover;

}
