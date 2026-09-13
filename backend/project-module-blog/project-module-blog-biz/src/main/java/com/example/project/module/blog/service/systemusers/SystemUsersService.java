package com.example.project.module.blog.service.systemusers;

import com.example.project.module.blog.dal.dataobject.systemusers.SystemUsers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;
import java.util.Set;

/**

* @description 针对表【system_users(用户信息表)】的数据库操作Service
* @createDate 2025-03-17 19:43:46
*/
public interface SystemUsersService extends IService<SystemUsers> {

    Map<Long, SystemUsers> getUserMap(Set<Long> userIds);
}
