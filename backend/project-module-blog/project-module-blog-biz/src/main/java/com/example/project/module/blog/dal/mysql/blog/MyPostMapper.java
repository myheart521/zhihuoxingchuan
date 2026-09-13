package com.example.project.module.blog.dal.mysql.blog;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.module.blog.controller.admin.blog.vo.PostPageReqVO;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客表（含审核、置顶状态） Mapper
 *

 */
@Mapper
public interface MyPostMapper extends BaseMapperX<MyPostDO> {

}
