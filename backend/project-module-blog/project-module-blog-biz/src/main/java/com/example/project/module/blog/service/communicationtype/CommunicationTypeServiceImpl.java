package com.example.project.module.blog.service.communicationtype;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.module.blog.controller.admin.communicationtype.vo.CommunicationTypePageReqVO;
import com.example.project.module.blog.controller.admin.communicationtype.vo.CommunicationTypeSaveReqVO;
import com.example.project.module.blog.dal.dataobject.communicationtype.CommunicationTypeDO;
import com.example.project.module.blog.dal.mysql.communicationtype.CommunicationTypeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.COMMUNICATION_TYPE_NOT_EXISTS;

/**
 * 群聊类型管理 Service 实现类
 *

 */
@Service
@Validated
public class CommunicationTypeServiceImpl implements CommunicationTypeService {

    @Resource
    private CommunicationTypeMapper communicationTypeMapper;

    @Override
    public Long createCommunicationType(CommunicationTypeSaveReqVO createReqVO) {
        // 插入
        CommunicationTypeDO communicationType = BeanUtils.toBean(createReqVO, CommunicationTypeDO.class);
        communicationTypeMapper.insert(communicationType);
        // 返回
        return communicationType.getId();
    }

    @Override
    public void updateCommunicationType(CommunicationTypeSaveReqVO updateReqVO) {
        // 校验存在
        validateCommunicationTypeExists(updateReqVO.getId());
        // 更新
        CommunicationTypeDO updateObj = BeanUtils.toBean(updateReqVO, CommunicationTypeDO.class);
        communicationTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteCommunicationType(Long id) {
        // 校验存在
        validateCommunicationTypeExists(id);
        // 删除
        communicationTypeMapper.deleteById(id);
    }

    private void validateCommunicationTypeExists(Long id) {
        if (communicationTypeMapper.selectById(id) == null) {
            throw exception(COMMUNICATION_TYPE_NOT_EXISTS);
        }
    }

    @Override
    public CommunicationTypeDO getCommunicationType(Long id) {
        return communicationTypeMapper.selectById(id);
    }

    @Override
    public PageResult<CommunicationTypeDO> getCommunicationTypePage(CommunicationTypePageReqVO pageReqVO) {
        return communicationTypeMapper.selectPage(pageReqVO);
    }

}
