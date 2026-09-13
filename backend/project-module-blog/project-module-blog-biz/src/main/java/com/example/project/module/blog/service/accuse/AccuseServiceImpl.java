package com.example.project.module.blog.service.accuse;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.security.core.util.SecurityFrameworkUtils;
import com.example.project.module.blog.controller.admin.accuse.vo.AccusePageReqVO;
import com.example.project.module.blog.controller.admin.accuse.vo.AccuseRespVO;
import com.example.project.module.blog.controller.admin.accuse.vo.AccuseSaveReqVO;
import com.example.project.module.blog.controller.admin.blog.vo.BlogDetailByIdRespVO;
import com.example.project.module.blog.dal.dataobject.accuse.AccuseDO;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import com.example.project.module.blog.dal.mysql.accuse.AccuseMapper;
import com.example.project.module.blog.dal.mysql.blog.MyPostMapper;
import com.example.project.module.blog.service.blog.BlogService;
import com.example.project.module.blog.service.post.MyPostService;
import com.example.project.module.system.controller.admin.user.vo.user.UserAccuseRespVO;
import com.example.project.module.system.controller.admin.user.vo.user.UserUpdateManageReqVO;
import com.example.project.module.system.dal.dataobject.permission.RoleDO;
import com.example.project.module.system.dal.dataobject.user.AdminUserDO;
import com.example.project.module.system.dal.mysql.user.AdminUserMapper;
import com.example.project.module.system.enums.permission.RoleCodeEnum;
import com.example.project.module.system.service.permission.RoleService;
import com.example.project.module.system.service.user.AdminUserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.*;


import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 博客举报记录 Service 实现类
 *

 */
@Service
@Validated
public class AccuseServiceImpl implements AccuseService {

    @Resource
    private AccuseMapper accuseMapper;

    @Resource
    private MyPostMapper myPostMapper;

    @Resource
    private MyPostService myPostService;

    @Resource
    private BlogService blogService;


    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private RoleService roleService;

    @Override
    public Long createAccuse(AccuseSaveReqVO createReqVO) {
        // 插入
        AccuseDO accuse = BeanUtils.toBean(createReqVO, AccuseDO.class);
        accuse.setUserId(createReqVO.getAccuseUserId());
        accuseMapper.insert(accuse);
        // 返回
        return accuse.getId();
    }

    @Override
    public void updateAccuse(AccuseSaveReqVO updateReqVO) {
        // 校验存在
        validateAccuseExists(updateReqVO.getId());


        if(updateReqVO.getProcessWay() == 1 || updateReqVO.getProcessWay() == 2){
            //如果修改的是admin直接报错退出
            if (updateReqVO.getAccusedUserId() == 1L) {
                throw exception(ACCUSE_ADMIN_USER);
            }
            //1.修改角色
            //如果想要修改发布博客的人的角色的话需要判断一下修改人是否有权修改或者能够修改
            //规则是：admin用户不能被修改，其他角色只能降级修改
            if (ObjectUtil.isNotEmpty(updateReqVO.getRoleCode())) {
                if (updateReqVO.getAccusedUserId() == 1L) {
                    throw exception(ACCUSE_ADMIN_USER);
                }
                if (Objects.equals(updateReqVO.getRoleCode(), RoleCodeEnum.SUPER_ADMIN.getCode())) {
                    throw exception(ACCUSE_ADMIN_FAIL);
                }
                if (Objects.equals(updateReqVO.getRoleCode(), RoleCodeEnum.BLOG_MANAGE.getCode())) {
                    throw exception(ACCUSE_ADMIN_FAIL);
                }
                if (Objects.equals(updateReqVO.getRoleCode(), RoleCodeEnum.BLOG_EXAMINE.getCode())) {
                    //判断用户权限字符是否是博客管理员
                    RoleDO roleDO = roleService.getRoleByUserId(updateReqVO.getAccusedUserId());
                    if (roleDO.getCode().equals(RoleCodeEnum.BLOG_MANAGE.getCode())) {
                        //是的话可以修改
                        UserUpdateManageReqVO updateManageReqVO = new UserUpdateManageReqVO();
                        updateManageReqVO.setNewCode(updateReqVO.getRoleCode());
                        updateManageReqVO.setUserId(updateReqVO.getAccusedUserId());
                        updateManageReqVO.setOldCode(roleDO.getCode());
                        adminUserService.updateUserManage(updateManageReqVO);
                    } else {
                        throw exception(ACCUSE_UN_ACCESS);
                    }
                }
                if (Objects.equals(updateReqVO.getRoleCode(), RoleCodeEnum.BLOG_UNEXAMINE.getCode())) {
                    //上面已经做出严格判断了，所以此处直接让修改就行了
                    RoleDO roleDO = roleService.getRoleByUserId(updateReqVO.getAccusedUserId());
                    UserUpdateManageReqVO updateManageReqVO = new UserUpdateManageReqVO();
                    updateManageReqVO.setNewCode(updateReqVO.getRoleCode());
                    updateManageReqVO.setUserId(updateReqVO.getAccusedUserId());
                    updateManageReqVO.setOldCode(roleDO.getCode());
                }
            }
            //2.修改用户账号状态
            if (updateReqVO.getIsBlock()) {
                adminUserService.updateUserStatus(updateReqVO.getAccusedUserId(), 1);
            }
            //3.修改用户的被举报的博客状态
            if (updateReqVO.getPostId() != null) {
                MyPostDO myPostDO = myPostMapper.selectById(updateReqVO.getPostId());
                if (myPostDO.getStatus().equals(2)) {
                    blogService.updateStatusById(updateReqVO.getPostId(), updateReqVO.getBlogStatus());
                }
            }
            //4.修改用户的积分
            if (updateReqVO.getScore() > 0) {
                AdminUserDO adminUserDO = adminUserMapper.selectById(updateReqVO.getAccusedUserId());
                //判断是否小于0
                if (adminUserDO.getReputation() - updateReqVO.getScore() < 0) {
                    //禁用账户
                    adminUserDO.setReputation(0);
                    adminUserService.updateUserStatus(updateReqVO.getAccusedUserId(), 1);
                }else{
                    adminUserDO.setReputation(adminUserDO.getReputation()-updateReqVO.getScore());
                }
                adminUserMapper.updateById(adminUserDO);
            }
        }
        AccuseDO accuseDO = new AccuseDO();
        accuseDO.setId(updateReqVO.getId());
        accuseDO.setStatus(1);
        accuseDO.setHandleTime(LocalDateTime.now());
        accuseDO.setProcessWay(updateReqVO.getProcessWay());
        accuseDO.setHandleUserId(SecurityFrameworkUtils.getLoginUserId());
        // 更新
        accuseMapper.updateById(accuseDO);
    }

    @Override
    public void deleteAccuse(Long id) {
        // 校验存在
        validateAccuseExists(id);
        // 删除
        accuseMapper.deleteById(id);
    }

    private void validateAccuseExists(Long id) {
        if (accuseMapper.selectById(id) == null) {
            throw exception(ACCUSE_NOT_EXISTS);
        }
    }

    @Override
    public AccuseDO getAccuse(Long id) {
        return accuseMapper.selectById(id);
    }

    @Override
    public PageResult<AccuseRespVO> getAccusePage(AccusePageReqVO pageReqVO) {

        PageResult<AccuseDO> accuseDOPageResult = accuseMapper.selectPage(pageReqVO);
        PageResult<AccuseRespVO> pageResult = BeanUtils.toBean(accuseDOPageResult, AccuseRespVO.class);
        List<AccuseRespVO> accuseRespVOList = new ArrayList<>();
        for (AccuseDO accuseDO : accuseDOPageResult.getList()) {
            //获取每条举报中的被举报id和举报人id
            Long postId = accuseDO.getPostId();
            Long userId = accuseDO.getUserId();
            //根据博客id获取被举报人信息
            MyPostDO myPostDO = myPostMapper.selectById(postId);

            AdminUserDO accusedPerson = adminUserMapper.selectById(myPostDO.getUserId());
            UserAccuseRespVO userAccusedRespVO = BeanUtils.toBean(accusedPerson, UserAccuseRespVO.class);
            //根据id获取举报人信息
            AdminUserDO accusePerson = adminUserMapper.selectById(userId);
            UserAccuseRespVO userAccuseRespVO = BeanUtils.toBean(accusePerson, UserAccuseRespVO.class);
            AccuseRespVO accuseRespVO = BeanUtils.toBean(accuseDO, AccuseRespVO.class);

            //赋值
            accuseRespVO.setAccuseUser(userAccuseRespVO);
            accuseRespVO.setAccusedUser(userAccusedRespVO);
            //获取举报人角色和被举报人角色并赋值
            accuseRespVO.setAccuseRole(roleService.getRoleByUserId(accusePerson.getId()).getCode());
            accuseRespVO.setAccusedRole(roleService.getRoleByUserId(accusedPerson.getId()).getCode());
            //根据处理人的id获取处理人的角色和名字
            if (accuseDO.getHandleUserId() != null) {
                accuseRespVO.setHandleUserRole(roleService.getRoleByUserId(accuseDO.getHandleUserId()).getCode());
                accuseRespVO.setHandleUserName(adminUserService.getUser(accuseDO.getHandleUserId()).getNickname());
            }
            accuseRespVOList.add(accuseRespVO);
        }
        pageResult.setList(accuseRespVOList);
        return pageResult;
    }

}
