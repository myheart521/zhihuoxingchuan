package com.example.project.module.blog.controller.admin.tag.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

import jakarta.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 博客标签新增/修改 Request VO")
@Data
public class TagSaveReqVO {

    @Schema(description = "标签ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18759")
    private Long id;

    @Schema(description = "标签名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "标签名称不能为空")
    private String name;

}
