package com.example.project.module.system.controller.admin.user.vo.user;

import com.example.project.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 管理员权限分页 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManageReqVO extends PageParam {
    @Schema(description = "搜索关键词（用户名/真实姓名）", example = "project")
    private String keyword;

    @Schema(description = "角色编码：blog_unexamine blog_examine blog_manage", example = "[blog_unexamine, blog_examine]")
    private String code;

    @Schema(description = "状态：0-正常 1-禁用", example = "0")
    private Integer status;
}
