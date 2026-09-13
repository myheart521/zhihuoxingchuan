package com.example.project.module.blog.service.postcategory;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.dal.dataobject.category.CategoryDO;
import com.example.project.module.blog.dal.mysql.category.CategoryMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.example.project.module.blog.controller.admin.postcategory.vo.*;
import com.example.project.module.blog.dal.dataobject.postcategory.PostCategoryDO;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.util.object.BeanUtils;

import com.example.project.module.blog.dal.mysql.postcategory.PostCategoryMapper;

import jakarta.annotation.Resource;

import javax.cache.annotation.CachePut;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.*;

/**
 * 博客与分类关联 Service 实现类
 *

 */
@Service
@Validated
public class PostCategoryServiceImpl implements PostCategoryService {

    @Resource
    private PostCategoryMapper postCategoryMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Long createPostCategory(PostCategorySaveReqVO createReqVO) {
        // 插入
        PostCategoryDO postCategory = BeanUtils.toBean(createReqVO, PostCategoryDO.class);
        postCategoryMapper.insert(postCategory);
        // 返回
        return postCategory.getId();
    }

    @Override

    public void updatePostCategory(PostCategorySaveReqVO updateReqVO) {
        // 校验存在
        validatePostCategoryExists(updateReqVO.getId());
        // 更新
        PostCategoryDO updateObj = BeanUtils.toBean(updateReqVO, PostCategoryDO.class);
        postCategoryMapper.updateById(updateObj);
    }

    @Override
    public void deletePostCategory(Long id) {
        // 校验存在
        validatePostCategoryExists(id);
        // 删除
        postCategoryMapper.deleteById(id);
    }

    private void validatePostCategoryExists(Long id) {
        if (postCategoryMapper.selectById(id) == null) {
            throw exception(POST_CATEGORY_NOT_EXISTS);
        }
    }

    @Override
    public PostCategoryDO getPostCategory(Long id) {
        return postCategoryMapper.selectById(id);
    }

    @Override
    public PageResult<PostCategoryDO> getPostCategoryPage(PostCategoryPageReqVO pageReqVO) {
        return postCategoryMapper.selectPage(pageReqVO);
    }

    @Override
    public List<Long> getPostIdsByCategoryId(Long categoryId) {
        LambdaQueryWrapper<PostCategoryDO> postCategoryDOLambdaQueryWrapper = new LambdaQueryWrapper<PostCategoryDO>();
        postCategoryDOLambdaQueryWrapper.eq(PostCategoryDO::getCategoryId, categoryId);
        List<PostCategoryDO> postCategoryDOS = postCategoryMapper.selectList(postCategoryDOLambdaQueryWrapper);
        List<Long> postIds = new ArrayList<>();
        postCategoryDOS.stream().map(PostCategoryDO::getPostId).forEach(postIds::add);
        return postIds;
    }

    @Override
    public List<Long> getCategoryIdsByPostId(Set<Long> blogIds) {
        LambdaQueryWrapper<PostCategoryDO> lambdaQueryWrapper = new LambdaQueryWrapperX<>();
        if (blogIds == null || blogIds.isEmpty()) {
            return new ArrayList<>();
        }
        lambdaQueryWrapper.in(PostCategoryDO::getPostId, blogIds);
        List<Long> categoryIds = new ArrayList<>();
        List<PostCategoryDO> postCategoryDOS = postCategoryMapper.selectList(lambdaQueryWrapper);
        postCategoryDOS.stream().map(PostCategoryDO::getCategoryId).forEach(categoryIds::add);
        return categoryIds;
    }

    @Override
    public PostCategoryDO selectByBlogId(Long id) {
        if (id == null) {
            return null;
        }
        return postCategoryMapper.selectOne(new LambdaQueryWrapperX<PostCategoryDO>().eq(PostCategoryDO::getPostId, id));
    }

    @Override
    public Long getCategoryIdByBlogId(Long id) {
        if (id == null) {
            return null;
        }
        //根据博客id获取分类id
        LambdaQueryWrapperX<PostCategoryDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<PostCategoryDO>();
        lambdaQueryWrapperX.eqIfPresent(PostCategoryDO::getPostId, id);
        PostCategoryDO entity = postCategoryMapper.selectOne(lambdaQueryWrapperX);
        return entity.getCategoryId();

    }

}
