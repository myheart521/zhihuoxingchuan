package com.example.project.module.blog.service.userlist;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.module.blog.controller.admin.userlist.vo.BlogUserListPageReqVO;
import com.example.project.module.blog.controller.admin.userlist.vo.BlogUserListSaveReqVO;
import com.example.project.module.blog.dal.dataobject.userlist.BlogUserListDO;
import com.example.project.module.blog.dal.mysql.userList.BlogUserListMapper;
import com.example.project.module.blog.service.userlist.BlogUserListService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.USER_LIST_NOT_EXISTS;

/**
 * 用户表（含角色、审核、信誉等信息） Service 实现类
 *

 */
@Service
@Validated
public class BlogUserListServiceImpl implements BlogUserListService {

    @Resource
    private BlogUserListMapper userListMapper;

    @Override
    public Long createUserList(BlogUserListSaveReqVO createReqVO) {
        // 插入
        BlogUserListDO userList = BeanUtils.toBean(createReqVO, BlogUserListDO.class);
        userListMapper.insert(userList);
        // 返回
        return userList.getId();
    }

    @Override
    public void updateUserList(BlogUserListSaveReqVO updateReqVO) {
        // 校验存在
        validateUserListExists(updateReqVO.getId());
        // 更新
        BlogUserListDO updateObj = BeanUtils.toBean(updateReqVO, BlogUserListDO.class);
        userListMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserList(Long id) {
        // 校验存在
        validateUserListExists(id);
        // 删除
        userListMapper.deleteById(id);
    }

    private void validateUserListExists(Long id) {
        if (userListMapper.selectById(id) == null) {
            throw exception(USER_LIST_NOT_EXISTS);
        }
    }

    @Override
    public BlogUserListDO getUserList(Long id) {
        return userListMapper.selectById(id);
    }

    @Override
    public PageResult<BlogUserListDO> getUserListPage(BlogUserListPageReqVO pageReqVO) {
        return userListMapper.selectPage(pageReqVO);
    }

}
