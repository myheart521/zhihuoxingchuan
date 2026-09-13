package com.example.project.module.blog.dal.mysql.posttag;

import java.util.*;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.module.blog.dal.dataobject.posttag.PostTagDO;
import org.apache.ibatis.annotations.Mapper;
import com.example.project.module.blog.controller.admin.posttag.vo.*;

/**
 * 博客标签关联 Mapper
 *

 */
@Mapper
public interface PostTagMapper extends BaseMapperX<PostTagDO> {

    default PageResult<PostTagDO> selectPage(PostTagPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PostTagDO>()
                .eqIfPresent(PostTagDO::getPostId, reqVO.getPostId())
                .eqIfPresent(PostTagDO::getTagId, reqVO.getTagId())
                .betweenIfPresent(PostTagDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PostTagDO::getId));
    }

}
