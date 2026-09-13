package com.example.project.module.blog.service.posttag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.dal.dataobject.posttag.PostTagDO;
import com.example.project.module.blog.dal.dataobject.tag.TagDO;
import com.example.project.module.blog.dal.mysql.posttag.PostTagMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import com.example.project.module.blog.controller.admin.posttag.vo.*;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.util.object.BeanUtils;


import jakarta.annotation.Resource;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.*;

/**
 * 博客标签关联 Service 实现类
 *

 */
@Service
@Validated
public class PostTagServiceImpl implements PostTagService {

    @Resource
    private PostTagMapper postTagMapper;

    @Override
    public Long createPostTag(PostTagSaveReqVO createReqVO) {
        // 插入
        PostTagDO postTag = BeanUtils.toBean(createReqVO, PostTagDO.class);
        postTagMapper.insert(postTag);
        // 返回
        return postTag.getId();
    }

    @Override
    public void updatePostTag(PostTagSaveReqVO updateReqVO) {
        // 校验存在
        validatePostTagExists(updateReqVO.getId());
        // 更新
        PostTagDO updateObj = BeanUtils.toBean(updateReqVO, PostTagDO.class);
        postTagMapper.updateById(updateObj);
    }

    @Override
    public void deletePostTag(Long id) {
        // 校验存在
        validatePostTagExists(id);
        // 删除
        postTagMapper.deleteById(id);
    }

    private void validatePostTagExists(Long id) {
        if (postTagMapper.selectById(id) == null) {
            throw exception(POST_TAG_NOT_EXISTS);
        }
    }

    @Override
    public PostTagDO getPostTag(Long id) {
        return postTagMapper.selectById(id);
    }

    @Override
    public PageResult<PostTagDO> getPostTagPage(PostTagPageReqVO pageReqVO) {
        return postTagMapper.selectPage(pageReqVO);
    }

    @Override
    public List<Long> getPostIdsByTagId(Long tagId) {
        LambdaQueryWrapperX<PostTagDO> postTagDOLambdaQueryWrapperX = new LambdaQueryWrapperX<PostTagDO>()
                .eqIfPresent(PostTagDO::getTagId, tagId);
        List<PostTagDO> postTagDOS = postTagMapper.selectList(postTagDOLambdaQueryWrapperX);
        List<Long> postIds = new ArrayList<>();
        postTagDOS.stream().map(PostTagDO::getPostId).forEach(postIds::add);
        return postIds;
    }

    @Override
    public List<Long> getTagIdsByPostIds(Set<Long> blogIds) {
        LambdaQueryWrapperX<PostTagDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<PostTagDO>();
        lambdaQueryWrapperX.inIfPresent(PostTagDO::getPostId, blogIds);
        List<PostTagDO> postTagDOS = postTagMapper.selectList(lambdaQueryWrapperX);
        List<Long> tagIds = new ArrayList<>();
        postTagDOS.stream().map(PostTagDO::getTagId).forEach(tagIds::add);
        return tagIds;
    }

    @Override
    public List<PostTagDO> selectByBlogId(Long id) {
        LambdaQueryWrapperX<PostTagDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<PostTagDO>();
        lambdaQueryWrapperX.eqIfPresent(PostTagDO::getPostId, id);
        return postTagMapper.selectList(lambdaQueryWrapperX);
    }

    @Override
    public List<Long> getTagIdsByBlogId(Long id) {
        LambdaQueryWrapperX<PostTagDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<PostTagDO>();
        lambdaQueryWrapperX.eqIfPresent(PostTagDO::getPostId, id);
        List<PostTagDO> postTagDOS = postTagMapper.selectList(lambdaQueryWrapperX);
        List<Long> tagIds = new ArrayList<>();
        postTagDOS.stream().map(PostTagDO::getTagId).forEach(tagIds::add);
        return tagIds;
    }

    @Override
    public void deleteByBlogId(Long id) {
        LambdaQueryWrapperX<PostTagDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<PostTagDO>();
        lambdaQueryWrapperX.eqIfPresent(PostTagDO::getPostId, id);
        postTagMapper.delete(lambdaQueryWrapperX);
    }

}
