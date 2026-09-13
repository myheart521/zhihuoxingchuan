package com.example.project.module.blog.service.index;

import com.example.project.module.blog.controller.admin.index.vo.BlogDetailRespVO;
import com.example.project.module.blog.controller.admin.index.vo.NoticeRespVO;
import com.example.project.module.blog.controller.admin.index.vo.SiteStatsRespVO;

import java.util.List;

public interface IndexService {
    List<NoticeRespVO> getNoticeList(Integer limit);

    String getRandomQuote();

    SiteStatsRespVO getSiteStats();

    BlogDetailRespVO getBlogDetail(Long id);
}
