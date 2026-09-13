package com.example.project.module.blog.service.tag;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.controller.admin.tag.vo.TagPageReqVO;
import com.example.project.module.blog.controller.admin.tag.vo.TagSaveReqVO;
import com.example.project.module.blog.dal.dataobject.category.CategoryDO;
import com.example.project.module.blog.dal.dataobject.posttag.PostTagDO;
import com.example.project.module.blog.dal.dataobject.tag.TagDO;
import com.example.project.module.blog.dal.mysql.posttag.PostTagMapper;
import com.example.project.module.blog.dal.mysql.tag.TagMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.TAG_NOT_EXISTS;

/**
 * 博客标签 Service 实现类
 *

 */
@Service
@Validated
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private PostTagMapper postTagMapper;

    @Override
    public Long createTag(TagSaveReqVO createReqVO) {
        // 插入
        TagDO tag = BeanUtils.toBean(createReqVO, TagDO.class);
        tagMapper.insert(tag);
        // 返回
        return tag.getId();
    }

    @Override
    public void updateTag(TagSaveReqVO updateReqVO) {
        // 校验存在
        validateTagExists(updateReqVO.getId());
        // 更新
        TagDO updateObj = BeanUtils.toBean(updateReqVO, TagDO.class);
        tagMapper.updateById(updateObj);
    }

    @Override
    public void deleteTag(Long id) {
        // 校验存在
        validateTagExists(id);
        // 删除
        tagMapper.deleteById(id);
    }

    private void validateTagExists(Long id) {
        if (tagMapper.selectById(id) == null) {
            throw exception(TAG_NOT_EXISTS);
        }
    }

    @Override
    public TagDO getTag(Long id) {
        return tagMapper.selectById(id);
    }

    @Override
    public PageResult<TagDO> getTagPage(TagPageReqVO pageReqVO) {
        return tagMapper.selectPage(pageReqVO);
    }

    @Override
    public List<TagDO> getTagList() {
        return tagMapper.selectList();
    }

    @Override
    public Map<Long, TagDO> getTagMapByBlogIds(Set<Long> tagIds) {
        LambdaQueryWrapperX<TagDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<TagDO>();
        lambdaQueryWrapperX.inIfPresent(TagDO::getId, tagIds);
        List<TagDO> tagDOList = tagMapper.selectList(lambdaQueryWrapperX);
        return tagDOList.stream().collect(Collectors.toMap(TagDO::getId, tag -> tag));
    }



}
