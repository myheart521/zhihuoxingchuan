package com.example.project.module.blog.service.blog;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.project.framework.common.constans.CacheConstants;
import com.example.project.framework.common.exception.ServiceException;
import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.collection.CollectionUtils;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.framework.common.util.object.ObjectUtils;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.framework.redis.core.CacheConfig;
import com.example.project.framework.redis.core.LayeredCache;
import com.example.project.framework.redis.core.LayeredCacheManager;
import com.example.project.framework.security.core.LoginUser;
import com.example.project.framework.security.core.util.SecurityFrameworkUtils;
import com.example.project.module.blog.controller.admin.blog.vo.*;
import com.example.project.module.blog.controller.admin.postcategory.vo.PostCategorySaveReqVO;
import com.example.project.module.blog.controller.admin.posttag.vo.PostTagSaveReqVO;
import com.example.project.module.blog.controller.admin.praise.vo.PraiseSaveReqVO;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import com.example.project.module.blog.dal.dataobject.category.CategoryDO;
import com.example.project.module.blog.dal.dataobject.postcategory.PostCategoryDO;
import com.example.project.module.blog.dal.dataobject.posttag.PostTagDO;
import com.example.project.module.blog.dal.dataobject.praise.PraiseDO;
import com.example.project.module.blog.dal.dataobject.systemusers.SystemUsers;
import com.example.project.module.blog.dal.dataobject.tag.TagDO;
import com.example.project.module.blog.dal.mysql.blog.MyPostMapper;
import com.example.project.module.blog.dal.mysql.postcategory.PostCategoryMapper;
import com.example.project.module.blog.dal.mysql.posttag.PostTagMapper;
import com.example.project.module.blog.dal.mysql.praise.PraiseMapper;
import com.example.project.module.blog.dal.mysql.systemusers.SystemUsersMapper;
import com.example.project.module.blog.dal.mysql.tag.TagMapper;
import com.example.project.module.blog.enums.ErrorCodeConstants;
import com.example.project.module.blog.service.category.CategoryService;
import com.example.project.module.blog.service.postcategory.PostCategoryService;
import com.example.project.module.blog.service.posttag.PostTagService;
import com.example.project.module.blog.service.praise.PraiseService;
import com.example.project.module.blog.service.systemuserrole.SystemUserRoleService;
import com.example.project.module.blog.service.systemusers.SystemUsersService;
import com.example.project.module.blog.service.tag.TagService;
import com.example.project.module.system.dal.dataobject.permission.RoleDO;
import com.example.project.module.system.dal.dataobject.user.AdminUserDO;
import com.example.project.module.system.dal.mysql.permission.RoleMapper;
import com.example.project.module.system.dal.mysql.user.AdminUserMapper;
import com.example.project.module.system.enums.permission.RoleCodeEnum;
import com.example.project.module.system.service.permission.PermissionService;
import com.example.project.module.system.service.permission.RoleService;
import com.example.project.module.system.service.user.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Validated
public class BlogServiceImpl implements BlogService {
    @Resource
    private MyPostMapper myPostMapper;

    @Resource
    private SystemUserRoleService systemUserRoleService;

    @Resource
    private PostTagService postTagService;

    @Resource
    private PostCategoryService postCategoryService;

    @Resource
    private SystemUsersService systemUsersService;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private PostTagMapper postTagMapper;

    @Resource
    private PostCategoryMapper postCategoryMapper;

    @Resource
    private SystemUsersMapper systemUsersMapper;

    @Resource
    private PermissionService permissionService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private PraiseMapper praiseMapper;

    @Resource
    private PraiseService praiseService;

    @Resource
    private AdminUserMapper adminUserMapper;


    @Autowired
    private CacheManager layeredCacheManager; // 这里会注入我们之前配置的LayeredCacheManager


    /**
     * 创建博客
     *
     * @param blogCreateReqVO 请求实体类
     * @return id、标题、状态
     */
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstants.PRACTICE_BLOG_PAGE, condition = "#blogCreateReqVO.isPractice == 1", allEntries = true),
            @CacheEvict(cacheNames = CacheConstants.Index_Blog_Page, condition = "#blogCreateReqVO.isPractice != 1", allEntries = true)})
//    @CacheEvict(cacheNames = "#blogCreateReqVO.isPractice == 1 ? T(com.example.project.framework.common.constans.CacheConstants).PRACTICE_BLOG_PAGE : T(com.example.project.framework.common.constans.CacheConstants).Index_Blog_Page",
//            allEntries = true)
    public BlogCreateRespVO createBlog(BlogCreateReqVO blogCreateReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<Long> tagIds = blogCreateReqVO.getTagIds();
        MyPostDO bean = BeanUtils.toBean(blogCreateReqVO, MyPostDO.class);
        //获取登录用户id
        if (loginUser != null) {
            Long id = loginUser.getId();
            bean.setUserId(id);
        }
        String code = roleService.getRoleByUserId(SecurityFrameworkUtils.getLoginUserId()).getCode();
        boolean isSuperAdmin = Objects.equals(code, RoleCodeEnum.SUPER_ADMIN.getCode());
        boolean isManager = Objects.equals(code, RoleCodeEnum.BLOG_MANAGE.getCode());
        //判断是否是管理员
        if (isSuperAdmin || isManager) {
            //管理员不用审核直接发布
            bean.setAuditUserId(1L);
            bean.setAuditComment("管理员发布，免审核");
            bean.setAuditTime(LocalDateTime.now());
            bean.setStatus(2);
        } else {
            //判断传递的status是否是0 || 1不是的话就报错
            if (blogCreateReqVO.getStatus() != 0 && blogCreateReqVO.getStatus() != 1) {
                throw exception(ErrorCodeConstants.BLOG_CREATE_FAIL);
            }
        }
        int inserted = myPostMapper.insert(bean);
        //更新博客标签关联表信息
        if (CollUtil.isNotEmpty(tagIds)) {
            for (Long tagId : tagIds) {
                PostTagSaveReqVO postTagSaveReqVO = new PostTagSaveReqVO();
                postTagSaveReqVO.setTagId(tagId);
                postTagSaveReqVO.setPostId(bean.getId());
                postTagService.createPostTag(postTagSaveReqVO);
            }
        }
        //更新博客分类关联表信息
        if (blogCreateReqVO.getCategoryId() != null) {
            PostCategorySaveReqVO postCategorySaveReqVO = new PostCategorySaveReqVO();
            postCategorySaveReqVO.setCategoryId(blogCreateReqVO.getCategoryId());
            postCategorySaveReqVO.setPostId(bean.getId());
            postCategoryService.createPostCategory(postCategorySaveReqVO);
        }

        if (inserted == 1) {
            //博客数量加1
            adminUserService.addBlogCount();
            return BeanUtils.toBean(bean, BlogCreateRespVO.class);
        } else {
            throw new ServiceException(ErrorCodeConstants.BLOG_CREATE_FAIL);
        }
    }

    /**
     * 获取博客分页
     *
     * @param pageReqVO 请求实体类
     * @return 博客分页
     */
    @Override
    @Cacheable(
            value = CacheConstants.Index_Blog_Page,
            key = "#pageReqVO.toCacheKey()"
    )
    public PageResult<BlogDetailRespVO> getBlogPage(PostPageReqVO pageReqVO) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(pageReqVO.getPageNo());
        pageParam.setPageSize(pageReqVO.getPageSize());
//        //获取用户id
//        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
//        //判断是否是admin
//
//        boolean admin = systemUserRoleService.isAdmin();
        //获取拥有该标签的博客id
        List<Long> blogIdsByTagId = new ArrayList<>();
        //判断tagId是否存在
        if (pageReqVO.getTagId() != null) {
            blogIdsByTagId = postTagService.getPostIdsByTagId(pageReqVO.getTagId());
            if (blogIdsByTagId.isEmpty()) {
                return new PageResult<>(new ArrayList<>(), 0L);
            }
        }
        //获取拥有该分类的博客id
        List<Long> blogIdsByCategoryId = new ArrayList<>();
        if (pageReqVO.getCategoryId() != null) {
            blogIdsByCategoryId = postCategoryService.getPostIdsByCategoryId(pageReqVO.getCategoryId());
            if (blogIdsByCategoryId.isEmpty()) {
                return new PageResult<>(new ArrayList<>(), 0L);
            }
        }
        LambdaQueryWrapperX<MyPostDO> queryWrapper = new LambdaQueryWrapperX<MyPostDO>();
        if (!blogIdsByTagId.isEmpty()) {
            queryWrapper.in(MyPostDO::getId, blogIdsByTagId);
        }
        if (!blogIdsByCategoryId.isEmpty()) {
            queryWrapper.in(MyPostDO::getId, blogIdsByCategoryId);
        }
        queryWrapper.likeIfPresent(MyPostDO::getTitle, pageReqVO.getKeyword())
                .eqIfPresent(MyPostDO::getStatus, pageReqVO.getStatus())
                .eq(MyPostDO::getIsPractice, 0)
                .eqIfPresent(MyPostDO::getVisibility, pageReqVO.getVisibility())
                .betweenIfPresent(MyPostDO::getCreateTime, pageReqVO.getStartTime(), pageReqVO.getEndTime())
                .orderByDesc(MyPostDO::getIsTop)
                .orderByDesc(MyPostDO::getCreateTime);

        PageResult<MyPostDO> myPostDOPageResult = myPostMapper
                .selectPage(pageParam, queryWrapper);
        //填充其他字段
        List<BlogDetailRespVO> blogDetailRespVOList = this.fillBlogInfo(myPostDOPageResult.getList());
        return new PageResult<>(blogDetailRespVOList, myPostDOPageResult.getTotal());
//        if (admin) {
//            PageResult<MyPostDO> myPostDOPageResult = myPostMapper
//                    .selectPage(pageParam, queryWrapper);
//            //填充其他字段
//            List<BlogDetailRespVO> blogDetailRespVOList = this.fillBlogInfo(myPostDOPageResult.getList());
//            return new PageResult<>(blogDetailRespVOList, myPostDOPageResult.getTotal());
//        } else {
//            if (loginUser != null) {
//                queryWrapper.eqIfPresent(MyPostDO::getUserId, loginUser.getId());
//            }
//            PageResult<MyPostDO> myPostDOPageResult = myPostMapper
//                    .selectPage(pageParam, queryWrapper);
//            //用户只能查询自己的博客
//            //填充其他字段
//            this.fillBlogInfo(myPostDOPageResult.getList());
//            //填充其他字段
//            List<BlogDetailRespVO> blogDetailRespVOList = this.fillBlogInfo(myPostDOPageResult.getList());
//            return new PageResult<>(blogDetailRespVOList, myPostDOPageResult.getTotal());
//        }
    }

    @Override
    public PageResult<BlogDetailRespVO> getBlogPersonalPage(PostPageReqVO pageReqVO) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(pageReqVO.getPageNo());
        pageParam.setPageSize(pageReqVO.getPageSize());
        //获取拥有该标签的博客id
        List<Long> blogIdsByTagId = new ArrayList<>();
        //判断tagId是否存在
        if (pageReqVO.getTagId() != null) {
            blogIdsByTagId = postTagService.getPostIdsByTagId(pageReqVO.getTagId());
            if (blogIdsByTagId.isEmpty()) {
                return new PageResult<>(new ArrayList<>(), 0L);
            }
        }
        //获取拥有该分类的博客id
        List<Long> blogIdsByCategoryId = new ArrayList<>();
        if (pageReqVO.getCategoryId() != null) {
            blogIdsByCategoryId = postCategoryService.getPostIdsByCategoryId(pageReqVO.getCategoryId());
            if (blogIdsByCategoryId.isEmpty()) {
                return new PageResult<>(new ArrayList<>(), 0L);
            }
        }
        LambdaQueryWrapperX<MyPostDO> queryWrapper = new LambdaQueryWrapperX<MyPostDO>();
        if (!blogIdsByTagId.isEmpty()) {
            queryWrapper.in(MyPostDO::getId, blogIdsByTagId);
        }
        if (!blogIdsByCategoryId.isEmpty()) {
            queryWrapper.in(MyPostDO::getId, blogIdsByCategoryId);
        }
        queryWrapper.likeIfPresent(MyPostDO::getTitle, pageReqVO.getKeyword())
                .eqIfPresent(MyPostDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(MyPostDO::getVisibility, pageReqVO.getVisibility())
                .betweenIfPresent(MyPostDO::getCreateTime, pageReqVO.getStartTime(), pageReqVO.getEndTime())
                .orderByDesc(MyPostDO::getIsTop)
                .orderByDesc(MyPostDO::getCreateTime);
        //获取用户id
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser != null) {
            queryWrapper.eqIfPresent(MyPostDO::getUserId, loginUser.getId());
        }
        PageResult<MyPostDO> myPostDOPageResult = myPostMapper
                .selectPage(pageParam, queryWrapper);
        //用户只能查询自己的博客
        //填充其他字段
        this.fillBlogInfo(myPostDOPageResult.getList());
        //填充其他字段
        List<BlogDetailRespVO> blogDetailRespVOList = this.fillBlogInfo(myPostDOPageResult.getList());
        return new PageResult<>(blogDetailRespVOList, myPostDOPageResult.getTotal());
    }

    @Override
    @Cacheable(
            value = CacheConstants.PRACTICE_BLOG_PAGE,
            key = "#pageReqVO.toCacheKey()"
    )
    public PageResult<BlogDetailRespVO> getBlogPracticePage(PostPageReqVO pageReqVO) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(pageReqVO.getPageNo());
        pageParam.setPageSize(pageReqVO.getPageSize());

        //获取拥有该标签的博客id
        List<Long> blogIdsByTagId = new ArrayList<>();
        //判断tagId是否存在
        if (pageReqVO.getTagId() != null) {
            blogIdsByTagId = postTagService.getPostIdsByTagId(pageReqVO.getTagId());
            if (blogIdsByTagId.isEmpty()) {
                return new PageResult<>(new ArrayList<>(), 0L);
            }
        }
        //获取拥有该分类的博客id
        List<Long> blogIdsByCategoryId = new ArrayList<>();
        if (pageReqVO.getCategoryId() != null) {
            blogIdsByCategoryId = postCategoryService.getPostIdsByCategoryId(pageReqVO.getCategoryId());
            if (blogIdsByCategoryId.isEmpty()) {
                return new PageResult<>(new ArrayList<>(), 0L);
            }
        }
        LambdaQueryWrapperX<MyPostDO> queryWrapper = new LambdaQueryWrapperX<MyPostDO>();
        if (!blogIdsByTagId.isEmpty()) {
            queryWrapper.in(MyPostDO::getId, blogIdsByTagId);
        }
        if (!blogIdsByCategoryId.isEmpty()) {
            queryWrapper.in(MyPostDO::getId, blogIdsByCategoryId);
        }
        queryWrapper.likeIfPresent(MyPostDO::getTitle, pageReqVO.getKeyword())
                .eqIfPresent(MyPostDO::getStatus, pageReqVO.getStatus())
                //此处设置实践主题
                .eq(MyPostDO::getIsPractice, 1)
                .eqIfPresent(MyPostDO::getVisibility, pageReqVO.getVisibility())
                .betweenIfPresent(MyPostDO::getCreateTime, pageReqVO.getStartTime(), pageReqVO.getEndTime())
                .orderByDesc(MyPostDO::getIsTop)
                .orderByDesc(MyPostDO::getLikeCount)
                .orderByDesc(MyPostDO::getCreateTime);

        PageResult<MyPostDO> myPostDOPageResult = myPostMapper
                .selectPage(pageParam, queryWrapper);
        //填充其他字段
        List<BlogDetailRespVO> blogDetailRespVOList = this.fillBlogInfo(myPostDOPageResult.getList());
        return new PageResult<>(blogDetailRespVOList, myPostDOPageResult.getTotal());
    }

    /**
     * @param id 博客id
     */
    @Override
//    @CacheEvict(cacheNames = CacheConstants.Index_Blog_Page, allEntries = true)
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstants.PRACTICE_BLOG_PAGE, condition = "#blogCreateReqVO.isPractice == 1", allEntries = true),
            @CacheEvict(cacheNames = CacheConstants.Index_Blog_Page, condition = "#blogCreateReqVO.isPractice != 1", allEntries = true)})
    public void deleteBlog(Long id) {
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.BLOG_DELETE_FAIL);
        }
        //判断是否是admin
        boolean admin = systemUserRoleService.isAdmin();
        if (admin) {
            myPostMapper.deleteById(id);
        } else {
            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
            //只能删除自己发过的博客
            if (loginUser != null) {
                Long userId = loginUser.getId();
                MyPostDO myPostDO = myPostMapper.selectById(id);
                if (myPostDO.getUserId().equals(userId)) {
                    myPostMapper.deleteById(id);
                }
            } else {
                throw new ServiceException(ErrorCodeConstants.BLOG_DELETE_FAIL);
            }
        }

    }

    /**
     * @param id    博客id
     * @param isTop 是否置顶 0-否 1-是
     */
    @Override
    @CacheEvict(cacheNames = CacheConstants.Index_Blog_Page, allEntries = true)
    public void isTopBlog(Long id, int isTop) {
        // 创建MyPostDO对象，并设置需要更新的字段
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.BLOG_ISTOP_FAIL);
        }
        MyPostDO myPostDO = new MyPostDO();
        myPostDO.setIsTop(isTop);
        //获取当前时间
        LocalDateTime topTime = LocalDateTime.now();
        myPostDO.setTopTime(topTime);
        // 构建更新条件
        LambdaUpdateWrapper<MyPostDO> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(MyPostDO::getId, id);
        //判断是否是admin
        boolean admin = systemUserRoleService.isAdmin();
        myPostMapper.update(myPostDO, updateWrapper);
//        if (admin) {
//            // 执行更新
//            myPostMapper.update(myPostDO, updateWrapper);
//        } else {
//            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
//            //只能删除自己发过的博客
//            if (loginUser != null) {
//                Long userId = loginUser.getId();
//                MyPostDO myPost = myPostMapper.selectById(id);
//                if (myPost.getUserId().equals(userId)) {
//                    // 执行更新
//                    myPostMapper.update(myPostDO, updateWrapper);
//                }
//            } else {
//                throw new ServiceException(ErrorCodeConstants.BLOG_ISTOP_FAIL);
//            }
//        }

    }

    /**
     * @param id         博客id
     * @param visibility 是否可见 0-公开 1-仅自己 2粉丝可见
     */
    @Override
    @CacheEvict(cacheNames = CacheConstants.Index_Blog_Page, allEntries = true)
    public void visibilityBlog(Long id, int visibility) {
        // 创建MyPostDO对象，并设置需要更新的字段
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.BLOG_VISIBILITY_FAIL);
        }
        MyPostDO myPostDO = new MyPostDO();
        myPostDO.setVisibility(visibility);
        // 构建更新条件
        LambdaUpdateWrapper<MyPostDO> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(MyPostDO::getId, id);
        //判断是否是admin
        boolean admin = systemUserRoleService.isAdmin();
        if (admin) {
            // 执行更新
            myPostMapper.update(myPostDO, updateWrapper);
        } else {
            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
            //只能更新自己发过的博客
            if (loginUser != null) {
                Long userId = loginUser.getId();
                MyPostDO myPost = myPostMapper.selectById(id);
                if (myPost.getUserId().equals(userId)) {
                    // 执行更新
                    myPostMapper.update(myPostDO, updateWrapper);
                }
            } else {
                throw new ServiceException(ErrorCodeConstants.BLOG_VISIBILITY_FAIL);

            }
        }


    }

    /**
     * 获取博客详情
     *
     * @param id 博客id
     * @return 博客详情
     */
    @Override
    public BlogDetailByIdRespVO detailBlog(Long id) {
        //根据博客id查询查询博客列表
        MyPostDO blog = myPostMapper.selectById(id);
        //判断博客是否通过审核、是否是草稿或者已下架
        if (blog.getStatus() != 2) {
            //未通过审核或者是草稿、下架的博客只有自己和管理员可以查看
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            if (!Objects.equals(loginUserId, blog.getUserId()) && !systemUserRoleService.isAdmin()) {
                throw new ServiceException(ErrorCodeConstants.BLOG_DETAIL_FAIL);
            }
        }

        BlogDetailByIdRespVO blogDetailByIdRespVO = BeanUtils.toBean(blog, BlogDetailByIdRespVO.class);
        //获取作者信息
        Long userId = blog.getUserId();
//        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
//        Long loginUserId = null;
//        if (loginUser != null) {
//            loginUserId = loginUser.getId();
//        } else {
//            throw new ServiceException(ErrorCodeConstants.BLOG_DETAIL_FAIL);
//        }
        AdminUserDO systemUsers = adminUserMapper.selectById(userId);
        BlogDetailByIdRespVO.Author author = BeanUtils.toBean(systemUsers, BlogDetailByIdRespVO.Author.class);
        Set<Long> userRoleIdListByUserId = permissionService.getUserRoleIdListByUserId(userId);
        //获取角色列表
        List<RoleDO> roleDOS = roleMapper.selectByIds(userRoleIdListByUserId);
        List<String> nameList = roleDOS.stream().map(RoleDO::getName).collect(Collectors.toList());
        List<String> codeList = roleDOS.stream().map(RoleDO::getCode).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(author)) {
            if (ObjectUtil.isNotEmpty(nameList)) {
                author.setName(nameList);
            }
            if (ObjectUtil.isNotEmpty(codeList)) {
                author.setCode(codeList);
            }
        }
        if (ObjectUtil.isNotEmpty(author)) {
            blogDetailByIdRespVO.setAuthor(author);
        }
        //获取分类信息
        Long categoryIdByBlogId = postCategoryService.getCategoryIdByBlogId(id);
        CategoryDO category = categoryService.getCategory(categoryIdByBlogId);
        BlogDetailByIdRespVO.Category bean = BeanUtils.toBean(category, BlogDetailByIdRespVO.Category.class);
        blogDetailByIdRespVO.setCategory(bean);
        //获取标签列表
        List<Long> tagIdsByBlogId = postTagService.getTagIdsByBlogId(id);
        List<TagDO> tagDOS = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(tagIdsByBlogId)) {
            tagDOS = tagMapper.selectByIds(tagIdsByBlogId);
        }
        List<BlogDetailByIdRespVO.Tag> tags = BeanUtils.toBean(tagDOS, BlogDetailByIdRespVO.Tag.class);
        blogDetailByIdRespVO.setTags(tags);
        //填充是否点赞过
        if (SecurityFrameworkUtils.getLoginUserId() != null) {
            boolean isPraise = isPraise(id, userId);
            blogDetailByIdRespVO.setPraise(isPraise);
        }
        //博客观看人数加1
        this.addViewCount(id);
        return blogDetailByIdRespVO;
    }

    /**
     * 审核博客
     *
     * @param id           博客id
     * @param status       博客状态
     * @param auditComment 审核意见
     */
    @Override
    @CacheEvict(cacheNames = CacheConstants.Index_Blog_Page, allEntries = true)
    public void examineBlog(Long id, int status, String auditComment) {
        MyPostDO blog = myPostMapper.selectById(id);
        if (blog.getStatus() == 1) {
            blog.setStatus(status);
            blog.setAuditComment(auditComment);
            blog.setAuditTime(LocalDateTime.now());
            blog.setAuditUserId(SecurityFrameworkUtils.getLoginUserId());
            LambdaQueryWrapperX<MyPostDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
            lambdaQueryWrapperX.eqIfPresent(MyPostDO::getId, id);
            myPostMapper.update(blog, lambdaQueryWrapperX);
        } else {
            throw new ServiceException(ErrorCodeConstants.BLOG_EXAMINE_FAIL);
        }
    }

    /**
     * 根据id更新博客状态
     *
     * @param postId 更新博客状态
     * @param status 博客状态
     */
    @Override
    public void updateStatusById(Long postId, int status) {
        //判断博客是否存在
        MyPostDO postDOValid = myPostMapper.selectById(postId);
        if (ObjectUtil.isEmpty(postDOValid)) {
            throw new ServiceException(ErrorCodeConstants.BLOG_NOT_EXIST);
        }
        MyPostDO postDO = new MyPostDO();
        postDO.setId(postId);
        postDO.setStatus(status);
        myPostMapper.updateById(postDO);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstants.PRACTICE_BLOG_PAGE, condition = "#blogCreateReqVO.isPractice == 1", allEntries = true),
            @CacheEvict(cacheNames = CacheConstants.Index_Blog_Page, condition = "#blogCreateReqVO.isPractice != 1", allEntries = true)})
    public void updateBlog(BlogCreateReqVO blogCreateReqVO) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (blogCreateReqVO.getId() == null) {
            throw new ServiceException(ErrorCodeConstants.BLOG_UPDATE_ID_NOT);
        }
        //判断博客是否存在
        MyPostDO postDOValid = myPostMapper.selectById(blogCreateReqVO.getId());
        if (ObjectUtil.isEmpty(postDOValid)) {
            throw new ServiceException(ErrorCodeConstants.BLOG_NOT_EXIST);
        }
        //判断博客是否是该用户所发，或者该用户是管理员
        //判断是否是管理员
        boolean isSuperAdmin = Objects.equals(roleService.getRoleByUserId(loginUserId).getCode(), RoleCodeEnum.SUPER_ADMIN.getCode());
        boolean isAdmin = Objects.equals(roleService.getRoleByUserId(loginUserId).getCode(), RoleCodeEnum.BLOG_MANAGE.getCode());
        if (!Objects.equals(SecurityFrameworkUtils.getLoginUserId(), blogCreateReqVO.getUserId()) && !isSuperAdmin && !isAdmin) {
            throw new ServiceException(ErrorCodeConstants.BLOG_UPDATE_FAIL);
        }
        //判断修改的博客状态是否为0||1
        if (Objects.equals(SecurityFrameworkUtils.getLoginUserId(), blogCreateReqVO.getUserId()) && blogCreateReqVO.getStatus() != 0 && blogCreateReqVO.getStatus() != 1) {
            throw exception(ErrorCodeConstants.BLOG_UPDATE_FAIL);
        }
        //执行更新操作
        MyPostDO postDO = new MyPostDO();
        BeanUtils.copyProperties(blogCreateReqVO, postDO);
        myPostMapper.updateById(postDO);
        //更新分类
        PostCategoryDO categoryDO = postCategoryService.selectByBlogId(blogCreateReqVO.getId());
        categoryDO.setCategoryId(blogCreateReqVO.getCategoryId());
        postCategoryMapper.updateById(categoryDO);
        //更新标签
        //先删除原有的
        postTagService.deleteByBlogId(blogCreateReqVO.getId());
        //再新增
        List<Long> tagIds = blogCreateReqVO.getTagIds();
        List<PostTagDO> postTagDOS = new ArrayList<>();
        for (Long tagId : tagIds) {
            PostTagDO postTagDO = new PostTagDO();
            postTagDO.setPostId(blogCreateReqVO.getId());
            postTagDO.setTagId(tagId);
            postTagDOS.add(postTagDO);
        }
        postTagMapper.insertBatch(postTagDOS);
    }

    @Override
    public void addLikeCount(Long postId) {
        MyPostDO myPostDO = myPostMapper.selectById(postId);
        if (ObjectUtil.isEmpty(myPostDO)) {
            throw new ServiceException(ErrorCodeConstants.BLOG_NOT_EXIST);
        }
        myPostDO.setLikeCount(myPostDO.getLikeCount() + 1);
        myPostMapper.updateById(myPostDO);
    }

    @Override
    public void addViewCount(Long postId) {
        MyPostDO myPostDO = myPostMapper.selectById(postId);
        if (ObjectUtil.isEmpty(myPostDO)) {
            throw new ServiceException(ErrorCodeConstants.BLOG_NOT_EXIST);
        }
        myPostDO.setViewCount(myPostDO.getViewCount() + 1);
        myPostMapper.updateById(myPostDO);
    }

    @Override
    public void praiseBlog(Long id, Long userId) {
        boolean isPraise = isPraise(id, userId);
        if (isPraise) {
            throw exception(ErrorCodeConstants.BLOG_PRAISE_FAIL);
        }
        //给用户点赞量加1
        adminUserService.addLikeCount();
        //给博客点赞量加1
        addLikeCount(id);
        //添加用户点赞人id
        PraiseSaveReqVO reqVO = new PraiseSaveReqVO();
        reqVO.setBlogId(id);
        reqVO.setUserId(userId);
        Long praise = praiseService.createPraise(reqVO);
    }

    @Override
    public Long getUserIdByBlogId(Long id) {
        LambdaQueryWrapperX<MyPostDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.eqIfPresent(MyPostDO::getId, id);
        MyPostDO myPostDO = myPostMapper.selectOne(lambdaQueryWrapperX);
        if (ObjectUtil.isNotEmpty(myPostDO)) {
            return myPostDO.getUserId();
        }
        return null;
    }

    @Override
    public List<MyPostDO> getBlogByUserId(Long userId) {
        LambdaQueryWrapperX<MyPostDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.eqIfPresent(MyPostDO::getUserId, userId);
        return myPostMapper.selectList(lambdaQueryWrapperX);

    }

    @Override
    public void uploadBlogToEs(BlogUploadESReqVO reqVO) {

    }

    @Override
    public List<BlogCarouselRespVO> getBlogPracticeCarousel() {
        //规则：先判断点赞量最多的，其次是置顶的，其次是浏览量
        LambdaQueryWrapperX<MyPostDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.eqIfPresent(MyPostDO::getIsPractice, 1);
        lambdaQueryWrapperX.eqIfPresent(MyPostDO::getStatus, 2);
        lambdaQueryWrapperX.eqIfPresent(MyPostDO::getVisibility, 0);
        //最多查询6条数据
        lambdaQueryWrapperX.orderByDesc(MyPostDO::getLikeCount, MyPostDO::getIsTop, MyPostDO::getViewCount);
        lambdaQueryWrapperX.last("limit 6");
        List<MyPostDO> myPostDOS = myPostMapper.selectList(lambdaQueryWrapperX);
        return BeanUtils.toBean(myPostDOS, BlogCarouselRespVO.class);
    }

    /**
     * 判断用户是否点赞过该博客 true点赞过 false没点赞过
     */

    private boolean isPraise(Long id, Long userId) {
        //判断用户是否点过赞
        LambdaQueryWrapperX<PraiseDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.eqIfPresent(PraiseDO::getBlogId, id);
        lambdaQueryWrapperX.eqIfPresent(PraiseDO::getUserId, userId);
        PraiseDO praiseDO = praiseMapper.selectOne(lambdaQueryWrapperX);
        return ObjectUtil.isNotEmpty(praiseDO);
    }

    /**
     * 填充博客的关联信息
     */
    private List<BlogDetailRespVO> fillBlogInfo(List<MyPostDO> blogList) {
        // 1. 获取所有用户ID
        Set<Long> userIds = CollectionUtils.convertSet(blogList, MyPostDO::getUserId);
        Map<Long, SystemUsers> userMap = systemUsersService.getUserMap(userIds);
        // 2. 获取所有博客ID，用于查询标签
        Set<Long> blogIds = CollectionUtils.convertSet(blogList, MyPostDO::getId);
//        if (blogIds.isEmpty()) {
//            return null;
//        }
//        List<MyPostDO> myPostDOS = myPostMapper.selectByIds(blogIds);

        // 3. 根据博客id，获取所有分类ID
        List<Long> categoryIdsByPostId = postCategoryService.getCategoryIdsByPostId(blogIds);
        Set<Long> categoryIds = new HashSet<>(categoryIdsByPostId);

        Map<Long, CategoryDO> categoryMap = categoryService.getCategoryMap(categoryIds);
        // 5.根据博客id，获取所有的标签id
        List<Long> tagIdsByPostId = postTagService.getTagIdsByPostIds(blogIds);
        Set<Long> tagIds = new HashSet<>(tagIdsByPostId);
        Map<Long, TagDO> tagMap = tagService.getTagMapByBlogIds(tagIds);
        // 4. 填充信息
        List<BlogDetailRespVO> blogDetailRespVOList = new ArrayList<>();
        blogList.forEach(blog -> {
            BlogDetailRespVO blogDetailRespVO = new BlogDetailRespVO();
            BeanUtils.copyProperties(blog, blogDetailRespVO);
            // 填充作者信息
            SystemUsers user = userMap.get(blog.getUserId());
            if (user != null) {
                blogDetailRespVO.setAuthor(BeanUtils.toBean(user, BlogDetailRespVO.Author.class));
            }
            // 填充分类信息
            PostCategoryDO postCategoryDO = postCategoryService.selectByBlogId(blog.getId());
            CategoryDO category = categoryMap.get(postCategoryDO.getCategoryId());
            if (category != null) {
                blogDetailRespVO.setCategory(BeanUtils.toBean(category, BlogDetailRespVO.Category.class));
            }
            // 填充标签信息
            List<PostTagDO> postTagDOList = postTagService.selectByBlogId(blog.getId());
            List<TagDO> tags = postTagDOList.stream()
                    .map(PostTagDO::getTagId)
                    .map(tagMap::get)
                    .collect(Collectors.toList());
            blogDetailRespVO.setTags(CollUtil.isNotEmpty(tags) ?
                    BeanUtils.toBean(tags, BlogDetailRespVO.Tag.class) :
                    new ArrayList<>());
            blogDetailRespVOList.add(blogDetailRespVO);
        });
        return blogDetailRespVOList;
    }

}
