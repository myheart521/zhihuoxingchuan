package com.example.project.module.blog.controller.admin.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "前台 - 网站统计信息 Response VO")
@Data
public class SiteStatsRespVO {
    @Schema(description = "总访问量")
    private Long totalVisits;

    @Schema(description = "文章总数")
    private Long totalPosts;

    @Schema(description = "用户总数")
    private Long totalUsers;

    @Schema(description = "运行天数")
    private Integer runningDays;
}
