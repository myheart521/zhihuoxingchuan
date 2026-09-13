package com.example.project.module.blog.service.userlist;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.userlist.vo.BlogUserListPageReqVO;
import com.example.project.module.blog.controller.admin.userlist.vo.BlogUserListSaveReqVO;
import com.example.project.module.blog.dal.dataobject.userlist.BlogUserListDO;

import jakarta.validation.Valid;

/**
 * 用户表（含角色、审核、信誉等信息） Service 接口
 *

 */
public interface BlogUserListService {

    /**
     * 创建用户表（含角色、审核、信誉等信息）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUserList(@Valid BlogUserListSaveReqVO createReqVO);

    /**
     * 更新用户表（含角色、审核、信誉等信息）
     *
     * @param updateReqVO 更新信息
     */
    void updateUserList(@Valid BlogUserListSaveReqVO updateReqVO);

    /**
     * 删除用户表（含角色、审核、信誉等信息）
     *
     * @param id 编号
     */
    void deleteUserList(Long id);

    /**
     * 获得用户表（含角色、审核、信誉等信息）
     *
     * @param id 编号
     * @return 用户表（含角色、审核、信誉等信息）
     */
    BlogUserListDO getUserList(Long id);

    /**
     * 获得用户表（含角色、审核、信誉等信息）分页
     *
     * @param pageReqVO 分页查询
     * @return 用户表（含角色、审核、信誉等信息）分页
     */
    PageResult<BlogUserListDO> getUserListPage(BlogUserListPageReqVO pageReqVO);

}
