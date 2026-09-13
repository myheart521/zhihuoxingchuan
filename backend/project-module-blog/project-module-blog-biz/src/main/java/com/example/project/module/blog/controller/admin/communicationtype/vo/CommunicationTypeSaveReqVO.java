package com.example.project.module.blog.controller.admin.communicationtype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 群聊类型管理新增/修改 Request VO")
@Data
public class CommunicationTypeSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "26088")
    private Long id;

    @Schema(description = "类型的名称", example = "芋艿")
    private String typeName;

    @Schema(description = "类型的描述")
    private String typeDes;

    @Schema(description = "类型的图片")
    private String typeImage;

    @Schema(description = "用来建立ws连接的类型（英文）", example = "2")
    private String type;

}
