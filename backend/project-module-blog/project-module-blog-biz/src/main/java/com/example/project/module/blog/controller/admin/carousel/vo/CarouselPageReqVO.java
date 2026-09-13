package com.example.project.module.blog.controller.admin.carousel.vo;

import com.example.project.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.example.project.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 轮播图分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarouselPageReqVO extends PageParam {

    @Schema(description = "图片URL")
    private String image;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述", example = "你猜")
    private String description;

    @Schema(description = "链接地址")
    private String link;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "状态：0-禁用 1-启用", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
