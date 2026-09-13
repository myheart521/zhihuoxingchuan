package com.example.project.module.blog.controller.admin.chatmessagesgroup.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.example.project.framework.excel.core.annotations.DictFormat;
import com.example.project.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户聊天记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ChatMessagesGroupRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "19851")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "发送者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17570")
    @ExcelProperty("发送者ID")
    private Long senderId;

    @Schema(description = "发送者头像")
    @ExcelProperty("发送者头像")
    private String senderAvatar;

    @Schema(description = "发送者名称", example = "赵六")
    @ExcelProperty("发送者名称")
    private String senderName;

    @Schema(description = "接收的群聊标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "接收的群聊标识", converter = DictConvert.class)
    @DictFormat("blog_chat_message_group") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer receiverGroup;

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("消息内容")
    private String text;

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "消息类型", converter = DictConvert.class)
    @DictFormat("blog_chat_group_message_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer messageType;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
