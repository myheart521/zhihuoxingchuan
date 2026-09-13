package com.example.project.module.blog.service.postcategory;

import java.util.*;

import com.example.project.module.blog.controller.admin.postcategory.vo.*;
import com.example.project.module.blog.dal.dataobject.postcategory.PostCategoryDO;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.pojo.PageParam;

import jakarta.validation.Valid;

/**
 * 博客与分类关联 Service 接口
 *

 */
public interface PostCategoryService {

    /**
     * 创建博客与分类关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPostCategory(@Valid PostCategorySaveReqVO createReqVO);

    /**
     * 更新博客与分类关联
     *
     * @param updateReqVO 更新信息
     */
    void updatePostCategory(@Valid PostCategorySaveReqVO updateReqVO);

    /**
     * 删除博客与分类关联
     *
     * @param id 编号
     */
    void deletePostCategory(Long id);

    /**
     * 获得博客与分类关联
     *
     * @param id 编号
     * @return 博客与分类关联
     */
    PostCategoryDO getPostCategory(Long id);

    /**
     * 获得博客与分类关联分页
     *
     * @param pageReqVO 分页查询
     * @return 博客与分类关联分页
     */
    PageResult<PostCategoryDO> getPostCategoryPage(PostCategoryPageReqVO pageReqVO);


    List<Long> getPostIdsByCategoryId(Long categoryId);

    List<Long> getCategoryIdsByPostId(Set<Long> blogIds);

    PostCategoryDO selectByBlogId(Long id);

    /**
     * 根据博客id获取分类
     */
    Long getCategoryIdByBlogId(Long id);


}
