package com.example.project.module.blog.dal.mysql.category;

import java.util.*;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.module.blog.dal.dataobject.category.CategoryDO;
import org.apache.ibatis.annotations.Mapper;
import com.example.project.module.blog.controller.admin.category.vo.*;

/**
 * 博客分类 Mapper
 *

 */
@Mapper
public interface CategoryMapper extends BaseMapperX<CategoryDO> {

    default List<CategoryDO> selectList(CategoryListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CategoryDO>()
                .likeIfPresent(CategoryDO::getName, reqVO.getName())
                .eqIfPresent(CategoryDO::getParentId, reqVO.getParentId())
                .orderByDesc(CategoryDO::getId));
    }

	default CategoryDO selectByParentIdAndName(Long parentId, String name) {
	    return selectOne(CategoryDO::getParentId, parentId, CategoryDO::getName, name);
	}

    default Long selectCountByParentId(Long parentId) {
        return selectCount(CategoryDO::getParentId, parentId);
    }

}