package com.example.project.module.blog.controller.admin.blog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "后台 - 将博客上传到ES Response VO")
@Data
public class BlogUploadESReqVO {
    @Schema(description = "博客id")
    private Long blogId;

    @Schema(description = "是否全部重新上传")
    private Boolean isAll;

    @Schema(description = "删除ES中全部的博客")
    private Boolean deleteAll;

    @Schema(description = "删除ES中的某些博客")
    private Boolean deleteBlogIds;
}
