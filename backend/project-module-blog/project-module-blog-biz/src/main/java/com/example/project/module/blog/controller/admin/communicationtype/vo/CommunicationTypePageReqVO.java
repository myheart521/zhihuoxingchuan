package com.example.project.module.blog.controller.admin.communicationtype.vo;

import com.example.project.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.example.project.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 群聊类型管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommunicationTypePageReqVO extends PageParam {

    @Schema(description = "类型的名称", example = "芋艿")
    private String typeName;

    @Schema(description = "类型的描述")
    private String typeDes;

    @Schema(description = "类型的图片")
    private String typeImage;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "用来建立ws连接的类型（英文）", example = "2")
    private String type;

}
