package com.example.project.module.blog.dal.mysql.carousel;

import java.util.*;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.module.blog.dal.dataobject.carousel.CarouselDO;
import org.apache.ibatis.annotations.Mapper;
import com.example.project.module.blog.controller.admin.carousel.vo.*;

/**
 * 轮播图 Mapper
 *

 */
@Mapper
public interface CarouselMapper extends BaseMapperX<CarouselDO> {

    default PageResult<CarouselDO> selectPage(CarouselPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CarouselDO>()
                .eqIfPresent(CarouselDO::getImage, reqVO.getImage())
                .eqIfPresent(CarouselDO::getTitle, reqVO.getTitle())
                .eqIfPresent(CarouselDO::getDescription, reqVO.getDescription())
                .eqIfPresent(CarouselDO::getLink, reqVO.getLink())
                .eqIfPresent(CarouselDO::getSort, reqVO.getSort())
                .eqIfPresent(CarouselDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CarouselDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CarouselDO::getId));
    }




}
