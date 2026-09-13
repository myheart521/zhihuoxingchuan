package com.example.project.module.blog.service.systemuserrole;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.project.module.blog.dal.dataobject.systemuserrole.SystemUserRole;

/**

* @description 针对表【system_user_role(用户和角色关联表)】的数据库操作Service
* @createDate 2025-03-17 19:21:48
*/
public interface SystemUserRoleService extends IService<SystemUserRole> {

    boolean isAdmin();
}
