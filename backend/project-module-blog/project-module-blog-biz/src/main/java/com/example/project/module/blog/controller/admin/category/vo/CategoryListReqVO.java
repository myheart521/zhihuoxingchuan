package com.example.project.module.blog.controller.admin.category.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.example.project.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 博客分类列表 Request VO")
@Data
public class CategoryListReqVO {

    @Schema(description = "分类名称", example = "赵六")
    private String name;

    @Schema(description = "父分类ID", example = "28407")
    private Long parentId;

}