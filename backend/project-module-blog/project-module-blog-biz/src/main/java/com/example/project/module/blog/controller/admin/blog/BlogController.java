package com.example.project.module.blog.controller.admin.blog;

import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.security.core.util.SecurityFrameworkUtils;
import com.example.project.module.blog.controller.admin.blog.vo.*;
import com.example.project.module.blog.controller.admin.category.vo.CategorySaveReqVO;
import com.example.project.module.blog.controller.admin.index.vo.BlogRespVO;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import com.example.project.module.blog.dal.mysql.blog.MyPostMapper;
import com.example.project.module.blog.service.blog.BlogService;
import com.example.project.module.system.dal.mysql.dept.PostMapper;
import com.example.project.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;

import javax.annotation.Nullable;
import java.util.List;

import static com.example.project.framework.common.pojo.CommonResult.success;
import static com.example.project.module.blog.enums.ErrorCodeConstants.BLOG_NOT_FOUND;
import static com.example.project.module.blog.enums.ErrorCodeConstants.BLOG_NOT_IS_YOU;

@Tag(name = "前台 - 博客")
@RestController
@RequestMapping("/blog/blog")
@Validated
public class BlogController {
    @Resource
    private BlogService blogService;

    @Resource
    private MyPostMapper myPostMapper;

    @Resource
    private AdminUserService adminUserService;

//    @Operation(summary = "获取个人待审核博客列表分页")
//    @GetMapping("/wait")
//    public CommonResult<PageResult<BlogDetailRespVO>> blogWaitListPage(@Valid PostPageReqVO reqVO) {
//        PageResult<BlogDetailRespVO> blogPage = blogService.getWaitBlogPage(reqVO);
//        return success(blogPage);
//    }

    @Operation(summary = "获取博客列表分页")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('blog:blog:query')")
    public CommonResult<PageResult<BlogDetailRespVO>> blogList(@Valid PostPageReqVO reqVO) {
        PageResult<BlogDetailRespVO> blogPage = blogService.getBlogPage(reqVO);
        return success(blogPage);
    }

    @Operation(summary = "上传博客到es中")
    @PostMapping("/upload/es")
    @PreAuthorize("@ss.hasPermission('blog:blog:upload')")
    public CommonResult<Boolean> uploadBlogToEs(@Valid @RequestBody BlogUploadESReqVO reqVO) {
        blogService.uploadBlogToEs(reqVO);
        return success(true);
    }

    @Operation(summary = "获取首页博客列表分页")
    @GetMapping("/index/page")
    public CommonResult<PageResult<BlogDetailRespVO>> blogIndexList(@Valid PostPageReqVO reqVO) {
        PageResult<BlogDetailRespVO> blogPage = blogService.getBlogPage(reqVO);
        //过滤草稿博客
        blogPage.getList().removeIf(blogDetailRespVO -> blogDetailRespVO.getStatus() == 0);
        return success(blogPage);
    }

    @Operation(summary = "获取个人博客列表分页")
    @GetMapping("/user/page")
    public CommonResult<PageResult<BlogDetailRespVO>> blogPersonalList(@Valid PostPageReqVO reqVO) {
        PageResult<BlogDetailRespVO> blogPage = blogService.getBlogPersonalPage(reqVO);
        return success(blogPage);
    }

    @Operation(summary = "获取实践主题博客列表分页")
    @GetMapping("/practice/page")
    public CommonResult<PageResult<BlogDetailRespVO>> blogPracticeList(@Valid PostPageReqVO reqVO) {
        PageResult<BlogDetailRespVO> blogPage = blogService.getBlogPracticePage(reqVO);
        return success(blogPage);
    }

    @Operation(summary = "获取实践博客轮播图")
    @GetMapping("/practice/carousel")
    public CommonResult<List<BlogCarouselRespVO>> blogPracticeCarousel() {
        List<BlogCarouselRespVO> blogPage = blogService.getBlogPracticeCarousel();
        return success(blogPage);
    }

    @PostMapping("/create")
    @Operation(summary = "新增博客")
    public CommonResult<BlogCreateRespVO> createBlog(@Valid @RequestBody BlogCreateReqVO blogCreateReqVO) {
        BlogCreateRespVO result = blogService.createBlog(blogCreateReqVO);
        return success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "更新博客")
    public CommonResult<Boolean> updateBlog(@Valid @RequestBody BlogCreateReqVO blogCreateReqVO) {
        blogService.updateBlog(blogCreateReqVO);
        return success(true);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除博客")
    @PreAuthorize("@ss.hasPermission('blog:blog:delete')")
    public CommonResult<Boolean> deleteBlog(@Valid @PathVariable("id") Long id) {
        blogService.deleteBlog(id);
        //博客数量减1
        adminUserService.reduceBlogCount();
        return success(true);
    }

    @DeleteMapping("/delete/user/{id}")
    @Operation(summary = "用户删除自己的个人博客")
    public CommonResult<Boolean> deleteUserBlog(@Valid @PathVariable("id") Long id) {
        //判断是否是该用户的博客
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        boolean equals = myPostMapper.selectById(id).getUserId().equals(loginUserId);
        if(!equals){
            throw exception(BLOG_NOT_IS_YOU);
        }
        blogService.deleteBlog(id);
        //博客数量减1
        adminUserService.reduceBlogCount();
        return success(true);
    }

    @PutMapping("/top/{id}")
    @Operation(summary = "置顶取消博客")
    @PreAuthorize("@ss.hasPermission('blog:blog:isTop')")
    public CommonResult<Boolean> isTopBlog(@Valid @PathVariable("id") Long id, @RequestParam("isTop") int isTop) {
        blogService.isTopBlog(id, isTop);
        return success(true);
    }

    @PutMapping("/visibility/{id}")
    @Operation(summary = "修改可见性博客")
    @PreAuthorize("@ss.hasPermission('blog:blog:visibility')")
    public CommonResult<Boolean> visibilityBlog(@Valid @PathVariable("id") Long id, @RequestParam("visibility") int visibility) {
        blogService.visibilityBlog(id, visibility);
        return success(true);
    }

    @PutMapping("/visibility/user/{id}")
    @Operation(summary = "用户自己修改可见性博客")
    public CommonResult<Boolean> visibilityUserBlog(@Valid @PathVariable("id") Long id, @RequestParam("visibility") int visibility) {
        //判断是否是该用户所发博客
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        boolean equals = myPostMapper.selectById(id).getUserId().equals(loginUserId);
        if(!equals){
            throw exception(BLOG_NOT_IS_YOU);
        }
        blogService.visibilityBlog(id, visibility);
        return success(true);
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "获取博客详情")
    public CommonResult<BlogDetailByIdRespVO> detailBlog(@Valid @PathVariable("id") Long id) {
        BlogDetailByIdRespVO blogDetailByIdRespVO = blogService.detailBlog(id);
        return success(blogDetailByIdRespVO);
    }

    @PutMapping("/examine/{id}/{status}")
    @Operation(summary = "修改博客状态")
    @PreAuthorize("@ss.hasPermission('blog:blog:examine')")
    public CommonResult<Boolean> examineBlog(@Valid @PathVariable("id") Long id, @PathVariable("status") int status, @RequestParam("auditComment") @Nullable String auditComment) {
        blogService.examineBlog(id, status, auditComment);
        return success(true);
    }

    @PostMapping("/praise")
    @Operation(summary = "给博客点赞")
    public CommonResult<Boolean> praiseBlog(@RequestParam("id") Long id) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        blogService.praiseBlog(id,loginUserId);
        return success(true);
    }
}
