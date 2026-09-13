package com.example.project.module.blog.controller.admin.carousel.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.example.project.framework.excel.core.annotations.DictFormat;
import com.example.project.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 轮播图 ResponseList VO")
@Data
@ExcelIgnoreUnannotated
public class CarouselRespListVo {
    @Schema(description = "轮播图ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24462")
    @ExcelProperty("轮播图ID")
    private Long id;

    @Schema(description = "图片URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("图片URL")
    private String image;

    @Schema(description = "标题")
    @ExcelProperty("标题")
    private String title;

    @Schema(description = "描述", example = "你猜")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "链接地址")
    @ExcelProperty("链接地址")
    private String link;

    @Schema(description = "排序号")
    @ExcelProperty("排序号")
    private Integer sort;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
