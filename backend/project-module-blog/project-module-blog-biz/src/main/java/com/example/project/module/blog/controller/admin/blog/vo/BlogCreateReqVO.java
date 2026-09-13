package com.example.project.module.blog.controller.admin.blog.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Schema(description = "前台 - 博客新增 Request VO")
@Data
public class BlogCreateReqVO {
     @Schema(description = "更新时使用的博客id", example = "666")
     private Long id;
//     @Schema(description = "博客状态只能传0-草稿 1-待审核", example = "666")
//     private Integer status;

     @Schema(description = "作者ID", example = "1")
     private Long userId;

     @Schema(description = "标题", example = "666")
     @NotNull(message = "标题不能为空")
     private String title;

     @Schema(description = "内容", example = "博客内容")
     @NotNull(message = "内容不能为空")
     @Size(min = 10, max = 60000, message = "内容长度必须在10到60000个字符之间")
     private String content;

     @Schema(description = "摘要", example = "博客摘要")
     @NotNull(message = "摘要不能为空")
     @Size(min = 5, max = 300, message = "内容长度必须在5到300个字符之间")
     private String summary;

     @Schema(description = "分类的id", example = "1")
     @NotNull(message = "请指定分类")
     private Long categoryId;

     @Schema(description = "标签的id", example = "[1,2,3]")
     @NotNull(message = "请指定标签")
     private List<Long> tagIds;

     @Schema(description = "可见度", example = "0")
     @NotNull(message = "请指定可见度")
     private Long visibility;

     @Schema(description = "状态 新增和更新 只能填0-草稿 1-待审核", example = "1")
     private Long status;

     @Schema(description = "封面图片URL", example = "https://example.invalid/resource")
     @NotNull(message = "请上传图片url")
     private String frontCover;


     @Schema(description = "是否是实践主题", example = "0")
     private Integer isPractice = 0;

}
