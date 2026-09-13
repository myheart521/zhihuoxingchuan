package com.example.project.module.blog.controller.admin.chatmessagesgroup.vo;

import com.example.project.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.example.project.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户聊天记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChatMessagesGroupPageReqVO extends PageParam {

    @Schema(description = "发送者名称", example = "赵六")
    private String senderName;

    @Schema(description = "接收的群聊标识", example = "1")
    private Integer receiverGroup;

    @Schema(description = "消息内容")
    private String text;

    @Schema(description = "消息类型", example = "1")
    private Integer messageType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}
