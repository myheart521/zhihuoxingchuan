package com.example.project.module.blog.service.post;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.blog.vo.PostPageReqVO;
import com.example.project.module.blog.controller.admin.blog.vo.PostSaveReqVO;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;

import jakarta.validation.Valid;

/**
 * 博客表（含审核、置顶状态） Service 接口
 *

 */
public interface MyPostService {

    /**
     * 创建博客表（含审核、置顶状态）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPost(@Valid PostSaveReqVO createReqVO);

    /**
     * 更新博客表（含审核、置顶状态）
     *
     * @param updateReqVO 更新信息
     */
    void updatePost(@Valid PostSaveReqVO updateReqVO);

    /**
     * 删除博客表（含审核、置顶状态）
     *
     * @param id 编号
     */
    void deletePost(Long id);

    /**
     * 获得博客表（含审核、置顶状态）
     *
     * @param id 编号
     * @return 博客表（含审核、置顶状态）
     */
    MyPostDO getPost(Long id);



}
