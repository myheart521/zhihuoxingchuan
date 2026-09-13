package com.example.project.module.blog.controller.admin.carousel;

import com.example.project.framework.apilog.core.annotation.ApiAccessLog;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.excel.core.util.ExcelUtils;
import com.example.project.framework.security.core.LoginUser;
import com.example.project.framework.security.core.util.SecurityFrameworkUtils;
import com.example.project.module.blog.controller.admin.carousel.vo.CarouselPageReqVO;
import com.example.project.module.blog.controller.admin.carousel.vo.CarouselRespListVo;
import com.example.project.module.blog.controller.admin.carousel.vo.CarouselRespVO;
import com.example.project.module.blog.controller.admin.carousel.vo.CarouselSaveReqVO;
import com.example.project.module.blog.dal.dataobject.carousel.CarouselDO;
import com.example.project.module.blog.service.carousel.CarouselService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.example.project.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.example.project.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 轮播图")
@RestController
@RequestMapping("/blog/carousel")
@Validated
public class CarouselController {

    @Resource
    private CarouselService carouselService;

    @PostMapping("/create")
    @Operation(summary = "创建轮播图")
    @PreAuthorize("@ss.hasPermission('blog:carousel:create')")
    public CommonResult<Long> createCarousel(@Valid @RequestBody CarouselSaveReqVO createReqVO) {
        return success(carouselService.createCarousel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新轮播图")
    @PreAuthorize("@ss.hasPermission('blog:carousel:update')")
    public CommonResult<Boolean> updateCarousel(@Valid @RequestBody CarouselSaveReqVO updateReqVO) {
        carouselService.updateCarousel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除轮播图")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:carousel:delete')")
    public CommonResult<Boolean> deleteCarousel(@RequestParam("id") Long id) {
        carouselService.deleteCarousel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得轮播图")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('blog:carousel:query')")
    public CommonResult<CarouselRespVO> getCarousel(@RequestParam("id") Long id) {
        CarouselDO carousel = carouselService.getCarousel(id);
        return success(BeanUtils.toBean(carousel, CarouselRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得轮播图分页")
    @PreAuthorize("@ss.hasPermission('blog:carousel:query')")
    public CommonResult<PageResult<CarouselRespVO>> getCarouselPage(@Valid CarouselPageReqVO pageReqVO) {
        PageResult<CarouselDO> pageResult = carouselService.getCarouselPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CarouselRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得全部未禁用轮播图")
    public CommonResult<List<CarouselRespListVo>> getCarouselList() {
        List<CarouselDO> resultList = carouselService.getCarouselList();
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser != null) {
            //LoginUser(id=1, userType=2, info={nickname=小帅, deptId=103}, tenantId=1, scopes=null, expiresTime=2025-03-18T20:38:13.374, context=null)
            System.out.println(loginUser);
            System.out.println(loginUser.getId());
            System.out.println(loginUser.getInfo());
            System.out.println(loginUser.getUserType());
        }
        return success(BeanUtils.toBean(resultList, CarouselRespListVo.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出轮播图 Excel")
    @PreAuthorize("@ss.hasPermission('blog:carousel:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCarouselExcel(@Valid CarouselPageReqVO pageReqVO,
                                    HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CarouselDO> list = carouselService.getCarouselPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "轮播图.xls", "数据", CarouselRespVO.class,
                BeanUtils.toBean(list, CarouselRespVO.class));
    }

}
