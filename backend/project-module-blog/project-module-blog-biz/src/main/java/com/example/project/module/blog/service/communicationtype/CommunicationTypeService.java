package com.example.project.module.blog.service.communicationtype;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.communicationtype.vo.CommunicationTypePageReqVO;
import com.example.project.module.blog.controller.admin.communicationtype.vo.CommunicationTypeSaveReqVO;
import com.example.project.module.blog.dal.dataobject.communicationtype.CommunicationTypeDO;
import jakarta.validation.Valid;

/**
 * 群聊类型管理 Service 接口
 *

 */
public interface CommunicationTypeService {

    /**
     * 创建群聊类型管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCommunicationType(@Valid CommunicationTypeSaveReqVO createReqVO);

    /**
     * 更新群聊类型管理
     *
     * @param updateReqVO 更新信息
     */
    void updateCommunicationType(@Valid CommunicationTypeSaveReqVO updateReqVO);

    /**
     * 删除群聊类型管理
     *
     * @param id 编号
     */
    void deleteCommunicationType(Long id);

    /**
     * 获得群聊类型管理
     *
     * @param id 编号
     * @return 群聊类型管理
     */
    CommunicationTypeDO getCommunicationType(Long id);

    /**
     * 获得群聊类型管理分页
     *
     * @param pageReqVO 分页查询
     * @return 群聊类型管理分页
     */
    PageResult<CommunicationTypeDO> getCommunicationTypePage(CommunicationTypePageReqVO pageReqVO);

}
