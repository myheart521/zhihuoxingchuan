package com.example.project.module.blog.controller.admin.communicationtype;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.excel.core.util.ExcelUtils;
import com.example.project.module.blog.controller.admin.communicationtype.vo.CommunicationTypePageReqVO;
import com.example.project.module.blog.controller.admin.communicationtype.vo.CommunicationTypeRespVO;
import com.example.project.module.blog.controller.admin.communicationtype.vo.CommunicationTypeSaveReqVO;
import com.example.project.module.blog.dal.dataobject.communicationtype.CommunicationTypeDO;
import com.example.project.module.blog.service.communicationtype.CommunicationTypeService;
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

@Tag(name = "管理后台 - 群聊类型管理")
@RestController
@RequestMapping("/blog/communication-type")
@Validated
public class CommunicationTypeController {

    @Resource
    private CommunicationTypeService communicationTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建群聊类型管理")
    @PreAuthorize("@ss.hasPermission('blog:communication-type:create')")
    public CommonResult<Long> createCommunicationType(@Valid @RequestBody CommunicationTypeSaveReqVO createReqVO) {
        return success(communicationTypeService.createCommunicationType(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新群聊类型管理")
    @PreAuthorize("@ss.hasPermission('blog:communication-type:update')")
    public CommonResult<Boolean> updateCommunicationType(@Valid @RequestBody CommunicationTypeSaveReqVO updateReqVO) {
        communicationTypeService.updateCommunicationType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除群聊类型管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:communication-type:delete')")
    public CommonResult<Boolean> deleteCommunicationType(@RequestParam("id") Long id) {
        communicationTypeService.deleteCommunicationType(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得群聊类型管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('blog:communication-type:query')")
    public CommonResult<CommunicationTypeRespVO> getCommunicationType(@RequestParam("id") Long id) {
        CommunicationTypeDO communicationType = communicationTypeService.getCommunicationType(id);
        return success(BeanUtils.toBean(communicationType, CommunicationTypeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得群聊类型管理分页")
    @PreAuthorize("@ss.hasPermission('blog:communication-type:query')")
    public CommonResult<PageResult<CommunicationTypeRespVO>> getCommunicationTypePage(@Valid CommunicationTypePageReqVO pageReqVO) {
        PageResult<CommunicationTypeDO> pageResult = communicationTypeService.getCommunicationTypePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CommunicationTypeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出群聊类型管理 Excel")
    @PreAuthorize("@ss.hasPermission('blog:communication-type:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCommunicationTypeExcel(@Valid CommunicationTypePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CommunicationTypeDO> list = communicationTypeService.getCommunicationTypePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "群聊类型管理.xls", "数据", CommunicationTypeRespVO.class,
                        BeanUtils.toBean(list, CommunicationTypeRespVO.class));
    }

}
