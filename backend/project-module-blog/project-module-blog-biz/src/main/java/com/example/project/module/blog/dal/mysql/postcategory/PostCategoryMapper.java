package com.example.project.module.blog.dal.mysql.postcategory;

import java.util.*;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.module.blog.dal.dataobject.postcategory.PostCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import com.example.project.module.blog.controller.admin.postcategory.vo.*;

/**
 * 博客与分类关联 Mapper
 *

 */
@Mapper
public interface PostCategoryMapper extends BaseMapperX<PostCategoryDO> {

    default PageResult<PostCategoryDO> selectPage(PostCategoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PostCategoryDO>()
                .eqIfPresent(PostCategoryDO::getPostId, reqVO.getPostId())
                .eqIfPresent(PostCategoryDO::getCategoryId, reqVO.getCategoryId())
                .betweenIfPresent(PostCategoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PostCategoryDO::getId));
    }

}
