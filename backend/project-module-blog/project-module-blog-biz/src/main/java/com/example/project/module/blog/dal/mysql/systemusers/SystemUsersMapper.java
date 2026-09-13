package com.example.project.module.blog.dal.mysql.systemusers;

import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.module.blog.dal.dataobject.category.CategoryDO;
import com.example.project.module.blog.dal.dataobject.systemusers.SystemUsers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**

* @description 针对表【system_users(用户信息表)】的数据库操作Mapper
* @createDate 2025-03-17 19:43:46
* @Entity com.example.project.module.blog.dal.dataobject.systemusers.SystemUsers
*/
@Mapper
public interface SystemUsersMapper extends BaseMapperX<SystemUsers> {

}




