package com.example.project.module.system.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.example.project.framework.common.enums.CommonStatusEnum;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.collection.CollectionUtils;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import com.example.project.module.system.controller.admin.permission.vo.role.RoleSaveReqVO;
import com.example.project.module.system.dal.dataobject.permission.RoleDO;
import com.example.project.module.system.dal.dataobject.permission.UserRoleDO;
import com.example.project.module.system.dal.mysql.permission.RoleMapper;
import com.example.project.module.system.dal.mysql.permission.UserRoleMapper;
import com.example.project.module.system.dal.redis.RedisKeyConstants;
import com.example.project.module.system.enums.permission.DataScopeEnum;
import com.example.project.module.system.enums.permission.RoleCodeEnum;
import com.example.project.module.system.enums.permission.RoleTypeEnum;
import com.google.common.annotations.VisibleForTesting;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.framework.common.util.collection.CollectionUtils.convertMap;
import static com.example.project.module.system.enums.ErrorCodeConstants.*;
import static com.example.project.module.system.enums.LogRecordConstants.*;

/**
 * 角色 Service 实现类
 *

 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    private PermissionService permissionService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = SYSTEM_ROLE_TYPE, subType = SYSTEM_ROLE_CREATE_SUB_TYPE, bizNo = "{{#role.id}}",
            success = SYSTEM_ROLE_CREATE_SUCCESS)
    public Long createRole(RoleSaveReqVO createReqVO, Integer type) {
        // 1. 校验角色
        validateRoleDuplicate(createReqVO.getName(), createReqVO.getCode(), null);

        // 2. 插入到数据库
        RoleDO role = BeanUtils.toBean(createReqVO, RoleDO.class)
                .setType(ObjectUtil.defaultIfNull(type, RoleTypeEnum.CUSTOM.getType()))
                .setStatus(ObjUtil.defaultIfNull(createReqVO.getStatus(), CommonStatusEnum.ENABLE.getStatus()))
                .setDataScope(DataScopeEnum.ALL.getScope()); // 默认可查看所有数据。原因是，可能一些项目不需要项目权限
        roleMapper.insert(role);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("role", role);
        return role.getId();
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#updateReqVO.id")
    @LogRecord(type = SYSTEM_ROLE_TYPE, subType = SYSTEM_ROLE_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = SYSTEM_ROLE_UPDATE_SUCCESS)
    public void updateRole(RoleSaveReqVO updateReqVO) {
        // 1.1 校验是否可以更新
        RoleDO role = validateRoleForUpdate(updateReqVO.getId());
        // 1.2 校验角色的唯一字段是否重复
        validateRoleDuplicate(updateReqVO.getName(), updateReqVO.getCode(), updateReqVO.getId());

        // 2. 更新到数据库
        RoleDO updateObj = BeanUtils.toBean(updateReqVO, RoleDO.class);
        roleMapper.updateById(updateObj);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(role, RoleSaveReqVO.class));
        LogRecordContext.putVariable("role", role);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新数据范围
        RoleDO updateObject = new RoleDO();
        updateObject.setId(id);
        updateObject.setDataScope(dataScope);
        updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        roleMapper.updateById(updateObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    @LogRecord(type = SYSTEM_ROLE_TYPE, subType = SYSTEM_ROLE_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = SYSTEM_ROLE_DELETE_SUCCESS)
    public void deleteRole(Long id) {
        // 1. 校验是否可以更新
        RoleDO role = validateRoleForUpdate(id);

        // 2.1 标记删除
        roleMapper.deleteById(id);
        // 2.2 删除相关数据
        permissionService.processRoleDeleted(id);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("role", role);
    }

    /**
     * 校验角色的唯一字段是否重复
     *
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id 角色编号
     */
    @VisibleForTesting
    void validateRoleDuplicate(String name, String code, Long id) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw exception(ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleDO role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw exception(ROLE_CODE_DUPLICATE, code);
        }
    }

    /**
     * 校验角色是否可以被更新
     *
     * @param id 角色编号
     */
    @VisibleForTesting
    RoleDO validateRoleForUpdate(Long id) {
        RoleDO role = roleMapper.selectById(id);
        if (role == null) {
            throw exception(ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.SYSTEM.getType().equals(role.getType())) {
            throw exception(ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
        return role;
    }

    @Override
    public RoleDO getRole(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.ROLE, key = "#id",
            unless = "#result == null")
    public RoleDO getRoleFromCache(Long id) {
        return roleMapper.selectById(id);
    }


    @Override
    public List<RoleDO> getRoleListByStatus(Collection<Integer> statuses) {
        return roleMapper.selectListByStatus(statuses);
    }

    @Override
    public List<RoleDO> getRoleList() {
        return roleMapper.selectList();
    }

    @Override
    public List<RoleDO> getRoleList(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleMapper.selectBatchIds(ids);
    }

    @Override
    public List<RoleDO> getRoleListFromCache(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 这里采用 for 循环从缓存中获取，主要考虑 Spring CacheManager 无法批量操作的问题
        RoleServiceImpl self = getSelf();
        return CollectionUtils.convertList(ids, self::getRoleFromCache);
    }

    @Override
    public PageResult<RoleDO> getRolePage(RolePageReqVO reqVO) {
        return roleMapper.selectPage(reqVO);
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return false;
        }
        RoleServiceImpl self = getSelf();
        return ids.stream().anyMatch(id -> {
            RoleDO role = self.getRoleFromCache(id);
            return role != null && RoleCodeEnum.isSuperAdmin(role.getCode());
        });
    }

    @Override
    public void validateRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得角色信息
        List<RoleDO> roles = roleMapper.selectBatchIds(ids);
        Map<Long, RoleDO> roleMap = convertMap(roles, RoleDO::getId);
        // 校验
        ids.forEach(id -> {
            RoleDO role = roleMap.get(id);
            if (role == null) {
                throw exception(ROLE_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())) {
                throw exception(ROLE_IS_DISABLE, role.getName());
            }
        });
    }

    @Override
    public RoleDO getRoleByUserId(Long userId) {
        // 1. 校验参数合法性 (防御性编程)
        if (userId == null || userId <= 0) {
            log.warn("Invalid userId: {}", userId);
            return getDefaultRole(); // 返回默认角色或抛业务异常
        }
        // 2. 查询用户角色关联关系
        List<UserRoleDO> userRoles = userRoleMapper.selectList(new LambdaQueryWrapperX<UserRoleDO>()
                .eq(UserRoleDO::getUserId, userId));
        // 3. 空集合防御逻辑修正
        if (CollectionUtil.isEmpty(userRoles)) {
            log.info("User [{}] has no role assigned, use default", userId);
            return getDefaultRole();
        }
        // 4. 提取角色ID（过滤无效roleId）
        List<Long> roleIds = userRoles.stream()
                .map(UserRoleDO::getRoleId)
                .filter(roleId -> roleId != null && roleId > 0)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(roleIds)) {
            log.warn("User [{}] has invalid roleIds, roles: {}", userId, userRoles);
            return getDefaultRole();
        }
        // 5. 批量查询角色信息（防御空集合）
        List<RoleDO> roles = roleMapper.selectByIds(roleIds);
        if (CollectionUtil.isEmpty(roles)) {
            log.warn("No valid roles found for ids: {}", roleIds);
            return getDefaultRole();
        }
        // 6. 按优先级确定角色
        return determineRoleByPriority(roles);
    }

// ------------------------ 关键子方法 ------------------------

    /**
     * 获取默认角色（系统兜底逻辑）
     */
    private RoleDO getDefaultRole() {
        return roleMapper.selectByCode(RoleCodeEnum.BLOG_UNEXAMINE.getCode());
    }

    /**
     * 根据角色优先级确定最终角色
     */
    private RoleDO determineRoleByPriority(List<RoleDO> roles) {
        // 预先定义允许的角色编码集合
        Set<String> allowedRoleCodes = new HashSet<>(Arrays.asList(
                RoleCodeEnum.SUPER_ADMIN.getCode(),
                RoleCodeEnum.BLOG_MANAGE.getCode(),
                RoleCodeEnum.BLOG_EXAMINE.getCode()
        ));

        return roles.stream()
                // STEP1：将角色转换为编码（防御空值）
                .map(RoleDO::getCode)
                .filter(Objects::nonNull)  // 防止code为null
                // STEP2：只保留允许的三个角色
                .filter(allowedRoleCodes::contains)
                // STEP3：按优先级寻找最高级（最小值即为最高优先级）
                .min(Comparator.comparingInt(this::getRolePriority))
                // STEP4：如果找到则查询完整角色信息，否则降级到默认角色
                .map(code -> roleMapper.selectByCode(code))
                .orElseGet(this::getDefaultRole);
    }

    /**
     * 角色优先级配置
     */
    private int getRolePriority(String roleCode) {
        if (RoleCodeEnum.SUPER_ADMIN.getCode().equals(roleCode)) {
            return 1; // 最高优先级
        } else if (RoleCodeEnum.BLOG_MANAGE.getCode().equals(roleCode)) {
            return 2;
        } else if (RoleCodeEnum.BLOG_EXAMINE.getCode().equals(roleCode)) {
            return 3;
        } else if (RoleCodeEnum.BLOG_UNEXAMINE.getCode().equals(roleCode)) {
            return 4; // 最低优先级
        } else {
            return Integer.MAX_VALUE; // 未知角色最低
        }
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private RoleServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

}
