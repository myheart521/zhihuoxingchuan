package com.example.project.module.blog.controller.admin.communicationtype.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 群聊类型管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CommunicationTypeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "26088")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "类型的名称", example = "芋艿")
    @ExcelProperty("类型的名称")
    private String typeName;

    @Schema(description = "类型的描述")
    @ExcelProperty("类型的描述")
    private String typeDes;

    @Schema(description = "类型的图片")
    @ExcelProperty("类型的图片")
    private String typeImage;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "用来建立ws连接的类型（英文）", example = "2")
    @ExcelProperty("用来建立ws连接的类型（英文）")
    private String type;

}
