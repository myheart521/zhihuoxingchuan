package com.example.project.module.blog.dal.mysql.tag;

import java.util.*;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.module.blog.dal.dataobject.tag.TagDO;
import org.apache.ibatis.annotations.Mapper;
import com.example.project.module.blog.controller.admin.tag.vo.*;

/**
 * 博客标签 Mapper
 *

 */
@Mapper
public interface TagMapper extends BaseMapperX<TagDO> {

    default PageResult<TagDO> selectPage(TagPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TagDO>()
                .likeIfPresent(TagDO::getName, reqVO.getName())
                .betweenIfPresent(TagDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TagDO::getId));
    }

}
