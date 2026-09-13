package com.example.project.module.blog.controller.admin.accuse;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.excel.core.util.ExcelUtils;
import com.example.project.module.blog.controller.admin.accuse.vo.AccusePageReqVO;
import com.example.project.module.blog.controller.admin.accuse.vo.AccuseRespVO;
import com.example.project.module.blog.controller.admin.accuse.vo.AccuseSaveReqVO;
import com.example.project.module.blog.dal.dataobject.accuse.AccuseDO;
import com.example.project.module.blog.service.accuse.AccuseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.example.project.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.example.project.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 博客举报记录")
@RestController
@RequestMapping("/blog/accuse")
@Validated
public class AccuseController {

    @Resource
    private AccuseService accuseService;

    @PostMapping("/create")
    @Operation(summary = "创建博客举报记录")
    public CommonResult<Long> createAccuse(@Valid @RequestBody AccuseSaveReqVO createReqVO) {
        return success(accuseService.createAccuse(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新博客举报记录")
    @PreAuthorize("@ss.hasPermission('blog:accuse:update')")
    public CommonResult<Boolean> updateAccuse(@Valid @RequestBody AccuseSaveReqVO updateReqVO) {
        accuseService.updateAccuse(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除博客举报记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:accuse:delete')")
    public CommonResult<Boolean> deleteAccuse(@RequestParam("id") Long id) {
        accuseService.deleteAccuse(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得博客举报记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('blog:accuse:query')")
    public CommonResult<AccuseRespVO> getAccuse(@RequestParam("id") Long id) {
        AccuseDO accuse = accuseService.getAccuse(id);
        return success(BeanUtils.toBean(accuse, AccuseRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得博客举报记录分页")
    @PreAuthorize("@ss.hasPermission('blog:accuse:query')")
    public CommonResult<PageResult<AccuseRespVO>> getAccusePage(@Valid AccusePageReqVO pageReqVO) {
        PageResult<AccuseRespVO> pageResult = accuseService.getAccusePage(pageReqVO);
        return success(pageResult);
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出博客举报记录 Excel")
    @PreAuthorize("@ss.hasPermission('blog:accuse:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAccuseExcel(@Valid AccusePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AccuseRespVO> list = accuseService.getAccusePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "博客举报记录.xls", "数据", AccuseRespVO.class,
                        BeanUtils.toBean(list, AccuseRespVO.class));
    }

}
