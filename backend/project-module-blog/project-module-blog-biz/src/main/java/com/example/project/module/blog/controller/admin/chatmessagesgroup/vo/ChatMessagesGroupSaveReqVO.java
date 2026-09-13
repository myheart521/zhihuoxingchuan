package com.example.project.module.blog.controller.admin.chatmessagesgroup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 用户聊天记录新增/修改 Request VO")
@Data
public class ChatMessagesGroupSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "19851")
    private Long id;

    @Schema(description = "发送者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17570")
    @NotNull(message = "发送者ID不能为空")
    private Long senderId;

    @Schema(description = "发送者头像")
    private String senderAvatar;

    @Schema(description = "发送者名称", example = "赵六")
    private String senderName;

    @Schema(description = "接收的群聊标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "接收的群聊标识不能为空")
    private Integer receiverGroup;

    @Schema(description = "消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "消息内容不能为空")
    private String text;

    @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "消息类型不能为空")
    private Integer messageType;

}
