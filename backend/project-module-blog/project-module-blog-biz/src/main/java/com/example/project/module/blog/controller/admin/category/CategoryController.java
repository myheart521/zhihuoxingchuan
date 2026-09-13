package com.example.project.module.blog.controller.admin.category;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
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

import com.example.project.module.blog.controller.admin.category.vo.*;
import com.example.project.module.blog.dal.dataobject.category.CategoryDO;
import com.example.project.module.blog.service.category.CategoryService;



import jakarta.validation.Valid;

@Tag(name = "管理后台 - 博客分类")
@RestController
@RequestMapping("/blog/category")
@Validated
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/test")
    @PermitAll
    public CommonResult<String> test() {
        return success("test");
    }

    @PostMapping("/create")
    @Operation(summary = "创建博客分类")
    @PreAuthorize("@ss.hasPermission('blog:category:create')")
    public CommonResult<Long> createCategory(@Valid @RequestBody CategorySaveReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新博客分类")
    @PreAuthorize("@ss.hasPermission('blog:category:update')")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody CategorySaveReqVO updateReqVO) {
        categoryService.updateCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除博客分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:category:delete')")
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得博客分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<CategoryRespVO> getCategory(@RequestParam("id") Long id) {
        CategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, CategoryRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得博客分类列表")
    public CommonResult<List<CategoryRespVO>> getCategoryList(@Valid CategoryListReqVO listReqVO) {
        List<CategoryDO> list = categoryService.getCategoryList(listReqVO);
        return success(BeanUtils.toBean(list, CategoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出博客分类 Excel")
    @PreAuthorize("@ss.hasPermission('blog:category:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCategoryExcel(@Valid CategoryListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<CategoryDO> list = categoryService.getCategoryList(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "博客分类.xls", "数据", CategoryRespVO.class,
                        BeanUtils.toBean(list, CategoryRespVO.class));
    }

}
