package com.example.project.module.blog.controller.admin.carousel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 轮播图新增/修改 Request VO")
@Data
public class CarouselSaveReqVO {

    @Schema(description = "轮播图ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24462")
    private Long id;

    @Schema(description = "图片URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "图片URL不能为空")
    private String image;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述", example = "你猜")
    private String description;

    @Schema(description = "链接地址")
    private String link;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "状态：0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态：0-禁用 1-启用不能为空")
    private Integer status;

}
