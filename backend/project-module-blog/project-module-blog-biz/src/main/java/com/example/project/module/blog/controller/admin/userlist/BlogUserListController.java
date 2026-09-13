package com.example.project.module.blog.controller.admin.userlist;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.excel.core.util.ExcelUtils;
import com.example.project.module.blog.controller.admin.userlist.vo.BlogUserListPageReqVO;
import com.example.project.module.blog.controller.admin.userlist.vo.BlogUserListRespVO;
import com.example.project.module.blog.controller.admin.userlist.vo.BlogUserListSaveReqVO;
import com.example.project.module.blog.dal.dataobject.userlist.BlogUserListDO;
import com.example.project.module.blog.service.userlist.BlogUserListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.example.project.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.example.project.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户表（含角色、审核、信誉等信息）")
@RestController
@RequestMapping("/blog/user-list")
@Validated
public class BlogUserListController {

    @Resource
    private BlogUserListService userListService;

    @PostMapping("/create")
    @Operation(summary = "创建用户表（含角色、审核、信誉等信息）")
    @PreAuthorize("@ss.hasPermission('blog:user-list:create')")
    public CommonResult<Long> createUserList(@Valid @RequestBody BlogUserListSaveReqVO createReqVO) {
        return success(userListService.createUserList(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户表（含角色、审核、信誉等信息）")
    @PreAuthorize("@ss.hasPermission('blog:user-list:update')")
    public CommonResult<Boolean> updateUserList(@Valid @RequestBody BlogUserListSaveReqVO updateReqVO) {
        userListService.updateUserList(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户表（含角色、审核、信誉等信息）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:user-list:delete')")
    public CommonResult<Boolean> deleteUserList(@RequestParam("id") Long id) {
        userListService.deleteUserList(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户表（含角色、审核、信誉等信息）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('blog:user-list:query')")
    public CommonResult<BlogUserListRespVO> getUserList(@RequestParam("id") Long id) {
        BlogUserListDO userList = userListService.getUserList(id);
        return success(BeanUtils.toBean(userList, BlogUserListRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户表（含角色、审核、信誉等信息）分页")
    @PreAuthorize("@ss.hasPermission('blog:user-list:query')")
    public CommonResult<PageResult<BlogUserListRespVO>> getUserListPage(@Valid BlogUserListPageReqVO pageReqVO) {
        PageResult<BlogUserListDO> pageResult = userListService.getUserListPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BlogUserListRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户表（含角色、审核、信誉等信息） Excel")
    @PreAuthorize("@ss.hasPermission('blog:user-list:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUserListExcel(@Valid BlogUserListPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BlogUserListDO> list = userListService.getUserListPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "用户表（含角色、审核、信誉等信息）.xls", "数据", BlogUserListRespVO.class,
                        BeanUtils.toBean(list, BlogUserListRespVO.class));
    }

}
