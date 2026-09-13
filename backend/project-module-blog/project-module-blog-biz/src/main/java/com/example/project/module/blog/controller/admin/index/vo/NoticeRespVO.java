package com.example.project.module.blog.controller.admin.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "前台 - 通知消息")
@Data
public class NoticeRespVO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "公告内容")
    private String content;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "优先级")
    private int priority;
}
