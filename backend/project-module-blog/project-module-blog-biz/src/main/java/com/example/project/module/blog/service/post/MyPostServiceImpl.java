package com.example.project.module.blog.service.post;

import com.example.project.framework.common.pojo.PageParam;
import com.example.project.framework.common.pojo.PageResult;
import com.example.project.framework.common.util.object.BeanUtils;
import com.example.project.module.blog.controller.admin.blog.vo.PostPageReqVO;
import com.example.project.module.blog.controller.admin.blog.vo.PostSaveReqVO;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import com.example.project.module.blog.dal.mysql.blog.MyPostMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import static com.example.project.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.example.project.module.blog.enums.ErrorCodeConstants.POST_NOT_EXISTS;

/**
 * 博客表（含审核、置顶状态） Service 实现类
 *

 */
@Service
@Validated
public class MyPostServiceImpl implements MyPostService {

    @Resource
    private MyPostMapper myPostMapper;

    @Override
    public Long createPost(PostSaveReqVO createReqVO) {
        // 插入
        MyPostDO post = BeanUtils.toBean(createReqVO, MyPostDO.class);
        myPostMapper.insert(post);
        // 返回
        return post.getId();
    }

    @Override
    public void updatePost(PostSaveReqVO updateReqVO) {
        // 校验存在
        validatePostExists(updateReqVO.getId());
        // 更新
        MyPostDO updateObj = BeanUtils.toBean(updateReqVO, MyPostDO.class);
        myPostMapper.updateById(updateObj);
    }

    @Override
    public void deletePost(Long id) {
        // 校验存在
        validatePostExists(id);
        // 删除
        myPostMapper.deleteById(id);
    }

    private void validatePostExists(Long id) {
        if (myPostMapper.selectById(id) == null) {
            throw exception(POST_NOT_EXISTS);
        }
    }

    @Override
    public MyPostDO getPost(Long id) {
        return myPostMapper.selectById(id);
    }



}
