package com.example.project.module.system.enums.permission;

import com.example.project.framework.common.util.object.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色标识枚举
 */
@Getter
@AllArgsConstructor
public enum RoleCodeEnum {

    SUPER_ADMIN("super_admin", "超级管理员"),
    TENANT_ADMIN("tenant_admin", "租户管理员"),
    CRM_ADMIN("crm_admin", "CRM 管理员"), // CRM 系统专用
    // 新增的博客系统角色
    BLOG_MANAGE("blog_manage", "博客管理员"),
    BLOG_EXAMINE("blog_examine", "已审核用户"),

    BLOG_UNEXAMINE("blog_unexamine","未审核用户");


    /**
     * 角色编码
     */
    private final String code;
    /**
     * 名字
     */
    private final String name;

    public static boolean isSuperAdmin(String code) {
        return ObjectUtils.equalsAny(code, SUPER_ADMIN.getCode());
    }

    // 新增通过编码获取枚举的方法（按需选择实现）
    public static RoleCodeEnum fromCode(String code) {
        for (RoleCodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
