package com.example.project.module.blog.service.systemuserrole;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.framework.security.core.util.SecurityFrameworkUtils;
import com.example.project.module.blog.dal.dataobject.systemuserrole.SystemUserRole;
import com.example.project.module.blog.dal.mysql.systemuserrole.SystemUserRoleMapper;
import org.springframework.stereotype.Service;

/**

* @description 针对表【system_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2025-03-17 19:21:48
*/
@Service
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRole>
    implements SystemUserRoleService{

    @Override
    public boolean isAdmin() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        return this.lambdaQuery().eq(SystemUserRole::getUserId, loginUserId).eq(SystemUserRole::getRoleId, 1).exists();
    }
}




