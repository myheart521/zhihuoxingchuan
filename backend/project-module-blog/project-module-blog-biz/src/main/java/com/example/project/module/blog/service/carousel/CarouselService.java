package com.example.project.module.blog.service.carousel;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.carousel.vo.CarouselPageReqVO;
import com.example.project.module.blog.controller.admin.carousel.vo.CarouselSaveReqVO;
import com.example.project.module.blog.dal.dataobject.carousel.CarouselDO;
import jakarta.validation.Valid;


import java.util.List;

/**
 * 轮播图 Service 接口
 *

 */
public interface CarouselService {

    /**
     * 创建轮播图
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCarousel(@Valid CarouselSaveReqVO createReqVO);

    /**
     * 更新轮播图
     *
     * @param updateReqVO 更新信息
     */
    void updateCarousel(@Valid CarouselSaveReqVO updateReqVO);

    /**
     * 删除轮播图
     *
     * @param id 编号
     */
    void deleteCarousel(Long id);

    /**
     * 获得轮播图
     *
     * @param id 编号
     * @return 轮播图
     */
    CarouselDO getCarousel(Long id);

    /**
     * 获得轮播图分页
     *
     * @param pageReqVO 分页查询
     * @return 轮播图分页
     */
    PageResult<CarouselDO> getCarouselPage(CarouselPageReqVO pageReqVO);

    List<CarouselDO> getCarouselList();
}
