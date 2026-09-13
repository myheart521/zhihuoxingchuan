package com.example.project.module.blog.service.posttag;

import java.util.*;
import com.example.project.module.blog.controller.admin.posttag.vo.*;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.module.blog.dal.dataobject.posttag.PostTagDO;

import jakarta.validation.Valid;

/**
 * 博客标签关联 Service 接口
 *

 */
public interface PostTagService {

    /**
     * 创建博客标签关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPostTag(@Valid PostTagSaveReqVO createReqVO);

    /**
     * 更新博客标签关联
     *
     * @param updateReqVO 更新信息
     */
    void updatePostTag(@Valid PostTagSaveReqVO updateReqVO);

    /**
     * 删除博客标签关联
     *
     * @param id 编号
     */
    void deletePostTag(Long id);

    /**
     * 获得博客标签关联
     *
     * @param id 编号
     * @return 博客标签关联
     */
    PostTagDO getPostTag(Long id);

    /**
     * 获得博客标签关联分页
     *
     * @param pageReqVO 分页查询
     * @return 博客标签关联分页
     */
    PageResult<PostTagDO> getPostTagPage(PostTagPageReqVO pageReqVO);

    /**
     * 根据标签id获取所有博客id
     */
    List<Long> getPostIdsByTagId(Long tagId);

    List<Long> getTagIdsByPostIds(Set<Long> blogIds);

    List<PostTagDO> selectByBlogId(Long id);

    List<Long> getTagIdsByBlogId(Long id);

    void deleteByBlogId(Long id);
}
