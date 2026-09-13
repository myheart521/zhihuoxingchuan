package com.example.project.module.blog.service.index;

import com.example.project.module.blog.controller.admin.index.vo.BlogDetailRespVO;
import com.example.project.module.blog.controller.admin.index.vo.NoticeRespVO;
import com.example.project.module.blog.controller.admin.index.vo.SiteStatsRespVO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class IndexServiceImpl implements IndexService{
    @Override
    public List<NoticeRespVO> getNoticeList(Integer limit) {
        return null;
    }

    @Override
    public String getRandomQuote() {
        return null;
    }

    @Override
    public SiteStatsRespVO getSiteStats() {
        return null;
    }

    @Override
    public BlogDetailRespVO getBlogDetail(Long id) {
        return null;
    }
}
