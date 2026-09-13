package com.example.project.module.blog.controller.admin.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "前台 - 轮播图 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CarouselRespVO {
    @Schema(description = "轮播图ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("轮播图ID")
    private Long id;

    @Schema(description = "图片URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("图片URL")
    private String image;

    @Schema(description = "标题")
    @ExcelProperty("标题")
    private String title;

    @Schema(description = "描述")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "链接地址")
    @ExcelProperty("链接地址")
    private String link;

    @Schema(description = "排序号")
    @ExcelProperty("排序号")
    private Integer sort;

    @Schema(description = "状态：0-禁用 1-启用")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private Date createTime;
}
