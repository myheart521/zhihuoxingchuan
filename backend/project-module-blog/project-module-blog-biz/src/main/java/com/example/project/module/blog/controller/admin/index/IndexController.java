package com.example.project.module.blog.controller.admin.index;

import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.module.blog.controller.admin.index.vo.*;
import com.example.project.module.blog.service.index.IndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.util.List;

import static com.example.project.framework.common.pojo.CommonResult.success;

@Tag(name = "前台 - 博客首页")
@RestController
@RequestMapping("/blog/index")
@Validated
public class IndexController {

    @Resource
    private IndexService indexService;



    @Operation(summary = "获取网站公告")
    @GetMapping("/notice/list")
    @PermitAll
    public CommonResult<List<NoticeRespVO>> noticeList(
            @Parameter(description = "获取条数") @RequestParam(defaultValue = "5") Integer limit) {
        return success(indexService.getNoticeList(limit));
    }

    @Operation(summary = "获取每日鸡汤")
    @GetMapping("/quote/random")
    @PermitAll
    public CommonResult<String> quoteRandom() {
        return success(indexService.getRandomQuote());
    }

    @Operation(summary = "获取网站统计信息")
    @GetMapping("/site/stats")
    @PermitAll
    public CommonResult<SiteStatsRespVO> siteStats() {
        return success(indexService.getSiteStats());
    }

    @Operation(summary = "博客阅读")
    @GetMapping("/detail/{id}")
    @PermitAll
    public CommonResult<BlogDetailRespVO> blogDetail(
            @Parameter(description = "博客ID", required = true) @PathVariable("id") Long id) {
        return success(indexService.getBlogDetail(id));
    }
}
