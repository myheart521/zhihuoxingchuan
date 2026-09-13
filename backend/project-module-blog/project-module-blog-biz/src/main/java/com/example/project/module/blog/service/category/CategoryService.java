package com.example.project.module.blog.service.category;

import com.example.project.module.blog.controller.admin.category.vo.CategoryListReqVO;
import com.example.project.module.blog.controller.admin.category.vo.CategorySaveReqVO;
import com.example.project.module.blog.dal.dataobject.category.CategoryDO;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 博客分类 Service 接口
 *

 */
public interface CategoryService {

    /**
     * 创建博客分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCategory(@Valid CategorySaveReqVO createReqVO);

    /**
     * 更新博客分类
     *
     * @param updateReqVO 更新信息
     */
    void updateCategory(@Valid CategorySaveReqVO updateReqVO);

    /**
     * 删除博客分类
     *
     * @param id 编号
     */
    void deleteCategory(Long id);

    /**
     * 获得博客分类
     *
     * @param id 编号
     * @return 博客分类
     */
    CategoryDO getCategory(Long id);

    /**
     * 获得博客分类列表
     *
     * @param listReqVO 查询条件
     * @return 博客分类列表
     */
    List<CategoryDO> getCategoryList(CategoryListReqVO listReqVO);

    Map<Long, CategoryDO> getCategoryMap(Set<Long> categoryIds);
}
