package com.example.project.module.blog.service.praise;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.praise.vo.PraisePageReqVO;
import com.example.project.module.blog.controller.admin.praise.vo.PraiseSaveReqVO;
import com.example.project.module.blog.dal.dataobject.praise.PraiseDO;

import jakarta.validation.Valid;

/**
 * 博客点赞 Service 接口
 *

 */
public interface PraiseService {

    /**
     * 创建博客点赞
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPraise(@Valid PraiseSaveReqVO createReqVO);

    /**
     * 更新博客点赞
     *
     * @param updateReqVO 更新信息
     */
    void updatePraise(@Valid PraiseSaveReqVO updateReqVO);

    /**
     * 删除博客点赞
     *
     * @param id 编号
     */
    void deletePraise(Long id);

    /**
     * 获得博客点赞
     *
     * @param id 编号
     * @return 博客点赞
     */
    PraiseDO getPraise(Long id);

    /**
     * 获得博客点赞分页
     *
     * @param pageReqVO 分页查询
     * @return 博客点赞分页
     */
    PageResult<PraiseDO> getPraisePage(PraisePageReqVO pageReqVO);

}
