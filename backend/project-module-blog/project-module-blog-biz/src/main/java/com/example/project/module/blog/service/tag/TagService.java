package com.example.project.module.blog.service.tag;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.tag.vo.TagPageReqVO;
import com.example.project.module.blog.controller.admin.tag.vo.TagSaveReqVO;
import com.example.project.module.blog.dal.dataobject.tag.TagDO;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 博客标签 Service 接口
 *

 */
public interface TagService {

    /**
     * 创建博客标签
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTag(@Valid TagSaveReqVO createReqVO);

    /**
     * 更新博客标签
     *
     * @param updateReqVO 更新信息
     */
    void updateTag(@Valid TagSaveReqVO updateReqVO);

    /**
     * 删除博客标签
     *
     * @param id 编号
     */
    void deleteTag(Long id);

    /**
     * 获得博客标签
     *
     * @param id 编号
     * @return 博客标签
     */
    TagDO getTag(Long id);

    /**
     * 获得博客标签分页
     *
     * @param pageReqVO 分页查询
     * @return 博客标签分页
     */
    PageResult<TagDO> getTagPage(TagPageReqVO pageReqVO);

    List<TagDO> getTagList();

    Map<Long, TagDO> getTagMapByBlogIds(Set<Long> tagIds);


}
