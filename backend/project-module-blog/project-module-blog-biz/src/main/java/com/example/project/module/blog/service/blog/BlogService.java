package com.example.project.module.blog.service.blog;

import com.example.project.framework.common.pojo.PageResult;
import com.example.project.module.blog.controller.admin.blog.vo.*;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import jakarta.validation.Valid;

import java.util.List;

public interface BlogService {
    /**
     * 创建博客
     *
     * @param blogCreateReqVO 请求实体类
     * @return id、标题、状态
     */
    BlogCreateRespVO createBlog(BlogCreateReqVO blogCreateReqVO);

    /**
     * 获取博客分页
     *
     * @param pageReqVO 请求实体类
     * @return 博客分页
     */
    PageResult<BlogDetailRespVO> getBlogPage(PostPageReqVO pageReqVO);
    /**
     * 获取用户个人博客分页
     *
     * @param pageReqVO 请求实体类
     * @return 博客分页
     */
    PageResult<BlogDetailRespVO> getBlogPersonalPage(PostPageReqVO pageReqVO);

    /**
     * 获取实践主题博客分页
     *
     * @param reqVO 请求实体类
     * @return 博客分页
     */
    PageResult<BlogDetailRespVO> getBlogPracticePage(@Valid PostPageReqVO reqVO);


    /**
     * @param id 博客id
     */
    void deleteBlog(Long id);

    /**
     * @param id    博客id
     * @param isTop 是否置顶 0-否 1-是
     */
    void isTopBlog(Long id, int isTop);

    /**
     * @param id         博客id
     * @param visibility 是否可见 0-公开 1-仅自己 2粉丝可见
     */
    void visibilityBlog(Long id, int visibility);

    /**
     * 获取博客详情
     *
     * @param id 博客id
     * @return 博客详情
     */
    BlogDetailByIdRespVO detailBlog(Long id);

    /**
     * 审核博客
     *
     * @param id           博客id
     * @param status       博客状态
     * @param auditComment 审核意见
     */
    void examineBlog(Long id,int status,String auditComment);

    /**
     * 根据id更新博客状态
     *
     * @param postId 更新博客状态
     * @param status 博客状态
     */
    void updateStatusById(Long postId, int status);

    void updateBlog(BlogCreateReqVO blogCreateReqVO);

    /**
     * 给某个博客点赞数量加1
     */
    void addLikeCount(Long postId);

    /**
     * 给某个博客观看人数加1
     */
    void addViewCount(Long postId);

    void praiseBlog(Long id,Long userId);

    /**
     * 根据博客id获取用户id
     */
    Long getUserIdByBlogId(Long id);


    /**
     * 根据用户id获取博客
     */
    List<MyPostDO> getBlogByUserId(Long userId);

    void uploadBlogToEs(BlogUploadESReqVO reqVO);

    /**
     * 获取实践主题博客轮播图
     * @return 博客轮播图
     */
    List<BlogCarouselRespVO> getBlogPracticeCarousel();
}
