package com.example.project.module.blog.service.praise;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.module.blog.controller.admin.praise.vo.*;
import com.example.project.module.blog.dal.dataobject.praise.PraiseDO;
import com.example.project.module.blog.dal.mysql.praise.PraiseMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.PRAISE_NOT_EXISTS;

/**
 * 博客点赞 Service 实现类
 *

 */
@Service
@Validated
public class PraiseServiceImpl implements PraiseService {

    @Resource
    private PraiseMapper praiseMapper;

    @Override
    public Long createPraise(PraiseSaveReqVO createReqVO) {
        // 插入
        PraiseDO praise = BeanUtils.toBean(createReqVO, PraiseDO.class);
        praiseMapper.insert(praise);
        // 返回
        return praise.getId();
    }

    @Override
    public void updatePraise(PraiseSaveReqVO updateReqVO) {
        // 校验存在
        validatePraiseExists(updateReqVO.getId());
        // 更新
        PraiseDO updateObj = BeanUtils.toBean(updateReqVO, PraiseDO.class);
        praiseMapper.updateById(updateObj);
    }

    @Override
    public void deletePraise(Long id) {
        // 校验存在
        validatePraiseExists(id);
        // 删除
        praiseMapper.deleteById(id);
    }

    private void validatePraiseExists(Long id) {
        if (praiseMapper.selectById(id) == null) {
            throw exception(PRAISE_NOT_EXISTS);
        }
    }

    @Override
    public PraiseDO getPraise(Long id) {
        return praiseMapper.selectById(id);
    }

    @Override
    public PageResult<PraiseDO> getPraisePage(PraisePageReqVO pageReqVO) {
        return praiseMapper.selectPage(pageReqVO);
    }

}
