package com.example.project.module.blog.controller.admin.tag;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import java.util.*;
import java.io.IOException;

import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.util.object.BeanUtils;
import static com.example.project.framework.common.pojo.CommonResult.success;

import com.example.project.framework.excel.core.util.ExcelUtils;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import static com.example.project.framework.apilog.core.enums.OperateTypeEnum.*;

import com.example.project.module.blog.controller.admin.tag.vo.*;
import com.example.project.module.blog.dal.dataobject.tag.TagDO;
import com.example.project.module.blog.service.tag.TagService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Tag(name = "管理后台 - 博客标签")
@RestController
@RequestMapping("/blog/tag")
@Validated
public class TagController {

    @Resource
    private TagService tagService;

    @PostMapping("/create")
    @Operation(summary = "创建博客标签")
    @PreAuthorize("@ss.hasPermission('blog:tag:create')")
    public CommonResult<Long> createTag(@Valid @RequestBody TagSaveReqVO createReqVO) {
        return success(tagService.createTag(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新博客标签")
    @PreAuthorize("@ss.hasPermission('blog:tag:update')")
    public CommonResult<Boolean> updateTag(@Valid @RequestBody TagSaveReqVO updateReqVO) {
        tagService.updateTag(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除博客标签")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:tag:delete')")
    public CommonResult<Boolean> deleteTag(@RequestParam("id") Long id) {
        tagService.deleteTag(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得博客标签")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<TagRespVO> getTag(@RequestParam("id") Long id) {
        TagDO tag = tagService.getTag(id);
        return success(BeanUtils.toBean(tag, TagRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得博客标签分页")
    public CommonResult<PageResult<TagRespVO>> getTagPage(@Valid TagPageReqVO pageReqVO) {
        PageResult<TagDO> pageResult = tagService.getTagPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TagRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得博客标签列表")
    public CommonResult<List<TagRespVO>> getTagList() {
        List<TagDO> resultList = tagService.getTagList();
        return success(BeanUtils.toBean(resultList, TagRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出博客标签 Excel")
    @PreAuthorize("@ss.hasPermission('blog:tag:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportTagExcel(@Valid TagPageReqVO pageReqVO,
                               HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TagDO> list = tagService.getTagPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "博客标签.xls", "数据", TagRespVO.class,
                BeanUtils.toBean(list, TagRespVO.class));
    }

}
