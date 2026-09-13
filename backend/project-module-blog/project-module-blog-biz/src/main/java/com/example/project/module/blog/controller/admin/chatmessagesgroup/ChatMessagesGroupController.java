package com.example.project.module.blog.controller.admin.chatmessagesgroup;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.excel.core.util.ExcelUtils;
import com.example.project.module.blog.controller.admin.chatmessagesgroup.vo.ChatMessagesGroupPageReqVO;
import com.example.project.module.blog.controller.admin.chatmessagesgroup.vo.ChatMessagesGroupRespVO;
import com.example.project.module.blog.controller.admin.chatmessagesgroup.vo.ChatMessagesGroupSaveReqVO;
import com.example.project.module.blog.dal.dataobject.chatmessagesgroup.ChatMessagesGroupDO;
import com.example.project.module.blog.service.chatmessagesgroup.ChatMessagesGroupService;
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

@Tag(name = "管理后台 - 用户聊天记录")
@RestController
@RequestMapping("/blog/chat-messages-group")
@Validated
public class ChatMessagesGroupController {

    @Resource
    private ChatMessagesGroupService chatMessagesGroupService;

    @PostMapping("/create")
    @Operation(summary = "创建用户聊天记录")
    @PreAuthorize("@ss.hasPermission('blog:chat-messages-group:create')")
    public CommonResult<Long> createChatMessagesGroup(@Valid @RequestBody ChatMessagesGroupSaveReqVO createReqVO) {
        return success(chatMessagesGroupService.createChatMessagesGroup(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户聊天记录")
    @PreAuthorize("@ss.hasPermission('blog:chat-messages-group:update')")
    public CommonResult<Boolean> updateChatMessagesGroup(@Valid @RequestBody ChatMessagesGroupSaveReqVO updateReqVO) {
        chatMessagesGroupService.updateChatMessagesGroup(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户聊天记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:chat-messages-group:delete')")
    public CommonResult<Boolean> deleteChatMessagesGroup(@RequestParam("id") Long id) {
        chatMessagesGroupService.deleteChatMessagesGroup(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户聊天记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('blog:chat-messages-group:query')")
    public CommonResult<ChatMessagesGroupRespVO> getChatMessagesGroup(@RequestParam("id") Long id) {
        ChatMessagesGroupDO chatMessagesGroup = chatMessagesGroupService.getChatMessagesGroup(id);
        return success(BeanUtils.toBean(chatMessagesGroup, ChatMessagesGroupRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户聊天记录分页")
    public CommonResult<PageResult<ChatMessagesGroupRespVO>> getChatMessagesGroupPage(@Valid ChatMessagesGroupPageReqVO pageReqVO) {
        PageResult<ChatMessagesGroupDO> pageResult = chatMessagesGroupService.getChatMessagesGroupPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ChatMessagesGroupRespVO.class));
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出用户聊天记录 Excel")
    @PreAuthorize("@ss.hasPermission('blog:chat-messages-group:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportChatMessagesGroupExcel(@Valid ChatMessagesGroupPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ChatMessagesGroupDO> list = chatMessagesGroupService.getChatMessagesGroupPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "用户聊天记录.xls", "数据", ChatMessagesGroupRespVO.class,
                        BeanUtils.toBean(list, ChatMessagesGroupRespVO.class));
    }

}
