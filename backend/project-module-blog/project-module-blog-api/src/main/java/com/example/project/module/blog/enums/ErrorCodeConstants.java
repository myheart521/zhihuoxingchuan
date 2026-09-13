package com.example.project.module.blog.enums;

import com.example.project.framework.common.exception.ErrorCode;

/**
 * Bpm 错误码枚举类
 * <p>
 * bpm 系统，使用 1-009-000-000 段
 */
public interface ErrorCodeConstants {
    // ========== 博客分类模块 1-012-001-000 ==========
    ErrorCode CATEGORY_NOT_EXISTS = new ErrorCode(1_012_001_000, "博客分类不存在");
    ErrorCode CATEGORY_EXITS_CHILDREN = new ErrorCode(1_012_001_001, "存在存在子博客分类，无法删除");
    ErrorCode CATEGORY_PARENT_NOT_EXITS = new ErrorCode(1_012_001_002, "父级博客分类不存在");
    ErrorCode CATEGORY_PARENT_ERROR = new ErrorCode(1_012_001_003, "不能设置自己为父博客分类");
    ErrorCode CATEGORY_NAME_DUPLICATE = new ErrorCode(1_012_001_004, "已经存在该分类名称的博客分类");
    ErrorCode CATEGORY_PARENT_IS_CHILD = new ErrorCode(1_012_001_005, "不能设置自己的子Category为父Category");

    // ========== 博客分类模块 1-012-002-000 ==========
    ErrorCode TAG_NOT_EXISTS = new ErrorCode(1_012_002_000, "博客标签不存在");
    // ========== 博客用户列表模块 1-012-003-000 ==========
    ErrorCode USER_LIST_NOT_EXISTS = new ErrorCode(1_012_003_000, "用户表（含角色、审核、信誉等信息）不存在");

    // ========== 博客用户列表模块 1-012-004-000 ==========
    ErrorCode POST_NOT_EXISTS = new ErrorCode(1_012_004_000, "博客表（含审核、置顶状态）不存在");


    // ========== 博客轮播图 1-012-005-000 ==========
    ErrorCode CAROUSEL_NOT_EXISTS = new ErrorCode(1_012_005_000, "轮播图不存在");

    // ========== 博客发布等模块 1-012-006-000 ==========
    ErrorCode BLOG_CREATE_FAIL = new ErrorCode(1_012_006_000, "博客新增失败");

    // ========== 博客与分类关联 1-012-007-000 ==========
    ErrorCode POST_CATEGORY_NOT_EXISTS = new ErrorCode(1_012_007_000, "博客与分类关联不存在");

    // ========== 博客标签关联 1-012-008-000 ==========
    ErrorCode POST_TAG_NOT_EXISTS = new ErrorCode(1_012_008_000, "博客标签关联不存在");
    // ========== 博客列表 1-012-009-000 ==========
    ErrorCode BLOG_DELETE_FAIL = new ErrorCode(1_012_009_000, "嘤嘤嘤，博客删除失败");
    ErrorCode BLOG_ISTOP_FAIL = new ErrorCode(1_012_009_001, "博客修改置顶状态失败");
    ErrorCode BLOG_VISIBILITY_FAIL = new ErrorCode(1_012_009_002, "博客修改可见状态失败");
    ErrorCode BLOG_EXAMINE_FAIL = new ErrorCode(1_012_009_003, "博客修改审核失败");
    ErrorCode BLOG_NOT_EXIST = new ErrorCode(1_012_009_004, "博客不存在呀呀呀");
    ErrorCode BLOG_UPDATE_FAIL = new ErrorCode(1_012_009_005, "博客更新失败");
    ErrorCode BLOG_UPDATE_ID_NOT = new ErrorCode(1_012_009_006, "更新博客的id缺失");
    ErrorCode BLOG_NOT_IS_YOU = new ErrorCode(1_012_009_006, "该博客不属于你");


    ErrorCode BLOG_NOT_FOUND = new ErrorCode(1_012_009_007, "未发现该博客");
    // ========== 角色信息 1-012-010-000 ==========
    ErrorCode ROLE_NOT_EXISTS = new ErrorCode(1_012_010_000, "角色信息不存在");
    // ========== 博客详情相关 1-012-011-000 ==========
    ErrorCode BLOG_DETAIL_FAIL = new ErrorCode(1_012_011_000, "获取博客详情失败");
    // ========== 用户和角色关联 1-012-012-000 ==========
    ErrorCode USER_ROLE_NOT_EXISTS = new ErrorCode(1_012_012_000, "用户和角色关联不存在");
    // ========== 评论相关 1-012-013-000 ==========
    ErrorCode COMMENT_NOT_EXISTS = new ErrorCode(1_012_013_000, "顶级评论不存在");
    ErrorCode COMMENT_REPLY_NOT_EXISTS = new ErrorCode(1_012_013_001, "二级评论回复不存在");
    // ========== 举报相关 1-012-014-000 ==========
    ErrorCode ACCUSE_NOT_EXISTS = new ErrorCode(1_012_014_000, "博客举报记录不存在");
    ErrorCode ACCUSE_ADMIN_USER = new ErrorCode(1_012_014_001, "big胆，不能对帅比admin角色进行修改");
    ErrorCode ACCUSE_ADMIN_FAIL = new ErrorCode(1_012_014_002, "我勒个扫杠，还把被举报的人变成管理员？");
    ErrorCode ACCUSE_UN_ACCESS = new ErrorCode(1_012_014_003, "只允许向下修改权限");

    // ========== 举报相关 1-012-015-000 ==========
    ErrorCode PRAISE_NOT_EXISTS = new ErrorCode(1_012_015_000, "博客点赞不存在");

    ErrorCode BLOG_PRAISE_FAIL = new ErrorCode(1_012_015_001, "已点过赞");

    // ========== 群聊相关 1-012-016-000 ==========
    ErrorCode COMMUNICATION_TYPE_NOT_EXISTS = new ErrorCode(1_012_015_000, "群聊类型管理不存在");
    ErrorCode CHAT_MESSAGES_GROUP_NOT_EXISTS = new ErrorCode(1_012_015_001, "用户聊天记录不存在");

}
