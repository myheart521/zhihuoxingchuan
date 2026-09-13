package com.example.project.module.blog.dal.mysql.userList;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.controller.admin.userlist.vo.BlogUserListPageReqVO;
import com.example.project.module.blog.dal.dataobject.userlist.BlogUserListDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表（含角色、审核、信誉等信息） Mapper
 *

 */
@Mapper
public interface BlogUserListMapper extends BaseMapperX<com.example.project.module.blog.dal.dataobject.userlist.BlogUserListDO> {

    default PageResult<BlogUserListDO> selectPage(BlogUserListPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<com.example.project.module.blog.dal.dataobject.userlist.BlogUserListDO>()
                .likeIfPresent(BlogUserListDO::getUsername, reqVO.getUsername())
                .eqIfPresent(BlogUserListDO::getPassword, reqVO.getPassword())
                .eqIfPresent(BlogUserListDO::getEmail, reqVO.getEmail())
                .eqIfPresent(BlogUserListDO::getRole, reqVO.getRole())
                .eqIfPresent(BlogUserListDO::getIsLocked, reqVO.getIsLocked())
                .eqIfPresent(BlogUserListDO::getReputation, reqVO.getReputation())
                .eqIfPresent(BlogUserListDO::getBlogCount, reqVO.getBlogCount())
                .eqIfPresent(BlogUserListDO::getLikeCount, reqVO.getLikeCount())
                .eqIfPresent(BlogUserListDO::getInviteCode, reqVO.getInviteCode())
                .likeIfPresent(BlogUserListDO::getRealName, reqVO.getRealName())
                .eqIfPresent(BlogUserListDO::getIdCardFront, reqVO.getIdCardFront())
                .eqIfPresent(BlogUserListDO::getIdCardBack, reqVO.getIdCardBack())
                .eqIfPresent(BlogUserListDO::getAuditStatus, reqVO.getAuditStatus())
                .eqIfPresent(BlogUserListDO::getAuditComment, reqVO.getAuditComment())
                .betweenIfPresent(BlogUserListDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BlogUserListDO::getId));
    }

}
