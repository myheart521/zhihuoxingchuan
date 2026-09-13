package com.example.project.module.blog.service.systemusers;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.project.module.blog.dal.dataobject.systemusers.SystemUsers;
import com.example.project.module.blog.dal.mysql.systemusers.SystemUsersMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**

 * @description 针对表【system_users(用户信息表)】的数据库操作Service实现
 * @createDate 2025-03-17 19:43:46
 */
@Service
public class SystemUsersServiceImpl extends ServiceImpl<SystemUsersMapper, SystemUsers>
        implements SystemUsersService {


    @Resource
    private SystemUsersMapper systemUsersMapper;

    @Override
    public Map<Long, SystemUsers> getUserMap(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return null;
        }
        List<SystemUsers> systemUsers = systemUsersMapper.selectByIds(userIds);
        //返回
        return systemUsers.stream().collect(Collectors.toMap(SystemUsers::getId, user -> user));
    }
}




