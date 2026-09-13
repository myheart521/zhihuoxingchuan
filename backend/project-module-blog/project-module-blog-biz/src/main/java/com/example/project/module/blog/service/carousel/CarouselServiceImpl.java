package com.example.project.module.blog.service.carousel;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.module.blog.controller.admin.carousel.vo.CarouselPageReqVO;
import com.example.project.module.blog.controller.admin.carousel.vo.CarouselSaveReqVO;
import com.example.project.module.blog.dal.dataobject.carousel.CarouselDO;
import com.example.project.module.blog.dal.mysql.carousel.CarouselMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.util.List;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.CAROUSEL_NOT_EXISTS;

/**
 * 轮播图 Service 实现类
 *

 */
@Service
@Validated
public class CarouselServiceImpl implements CarouselService {

    @Resource
    private CarouselMapper carouselMapper;

    @Override
    public Long createCarousel(CarouselSaveReqVO createReqVO) {
        // 插入
        CarouselDO carousel = BeanUtils.toBean(createReqVO, CarouselDO.class);
        carouselMapper.insert(carousel);
        // 返回
        return carousel.getId();
    }

    @Override
    public void updateCarousel(CarouselSaveReqVO updateReqVO) {
        // 校验存在
        validateCarouselExists(updateReqVO.getId());
        // 更新
        CarouselDO updateObj = BeanUtils.toBean(updateReqVO, CarouselDO.class);
        carouselMapper.updateById(updateObj);
    }

    @Override
    public void deleteCarousel(Long id) {
        // 校验存在
        validateCarouselExists(id);
        // 删除
        carouselMapper.deleteById(id);
    }

    private void validateCarouselExists(Long id) {
        if (carouselMapper.selectById(id) == null) {
            throw exception(CAROUSEL_NOT_EXISTS);
        }
    }

    @Override
    public CarouselDO getCarousel(Long id) {
        return carouselMapper.selectById(id);
    }

    @Override
    public PageResult<CarouselDO> getCarouselPage(CarouselPageReqVO pageReqVO) {
        return carouselMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CarouselDO> getCarouselList() {
        return carouselMapper.selectList(CarouselDO::getStatus, 0);
    }

}
