package com.example.project.module.blog.service.accuse;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.accuse.vo.AccusePageReqVO;
import com.example.project.module.blog.controller.admin.accuse.vo.AccuseRespVO;
import com.example.project.module.blog.controller.admin.accuse.vo.AccuseSaveReqVO;
import com.example.project.module.blog.dal.dataobject.accuse.AccuseDO;
import jakarta.validation.Valid;


/**
 * 博客举报记录 Service 接口
 *

 */
public interface AccuseService {

    /**
     * 创建博客举报记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAccuse(@Valid AccuseSaveReqVO createReqVO);

    /**
     * 更新博客举报记录
     *
     * @param updateReqVO 更新信息
     */
    void updateAccuse(@Valid AccuseSaveReqVO updateReqVO);

    /**
     * 删除博客举报记录
     *
     * @param id 编号
     */
    void deleteAccuse(Long id);

    /**
     * 获得博客举报记录
     *
     * @param id 编号
     * @return 博客举报记录
     */
    AccuseDO getAccuse(Long id);

    /**
     * 获得博客举报记录分页
     *
     * @param pageReqVO 分页查询
     * @return 博客举报记录分页
     */
    PageResult<AccuseRespVO> getAccusePage(AccusePageReqVO pageReqVO);

}
