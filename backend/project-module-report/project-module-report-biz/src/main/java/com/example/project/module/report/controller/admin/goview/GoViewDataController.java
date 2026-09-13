package com.example.project.module.report.controller.admin.goview;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.project.framework.common.pojo.CommonResult;
import com.example.project.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.example.project.module.blog.dal.dataobject.blog.MyPostDO;
import com.example.project.module.blog.dal.dataobject.category.CategoryDO;
import com.example.project.module.blog.dal.dataobject.postcategory.PostCategoryDO;
import com.example.project.module.blog.dal.dataobject.posttag.PostTagDO;
import com.example.project.module.blog.dal.dataobject.tag.TagDO;
import com.example.project.module.blog.dal.mysql.blog.MyPostMapper;
import com.example.project.module.blog.dal.mysql.category.CategoryMapper;
import com.example.project.module.blog.dal.mysql.postcategory.PostCategoryMapper;
import com.example.project.module.blog.dal.mysql.posttag.PostTagMapper;
import com.example.project.module.blog.dal.mysql.tag.TagMapper;
import com.example.project.module.report.controller.admin.goview.vo.data.GoViewDataGetBySqlReqVO;
import com.example.project.module.report.controller.admin.goview.vo.data.GoViewDataRespVO;
import com.example.project.module.report.service.goview.GoViewDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.project.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - GoView 数据", description = "提供 SQL、HTTP 等数据查询的能力")
@RestController
@RequestMapping("/report/go-view/data")
@Validated
public class GoViewDataController {

    @Resource
    private GoViewDataService goViewDataService;


    @Resource
    private MyPostMapper myPostMapper;

    @Resource
    private PostCategoryMapper postCategoryMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private PostTagMapper postTagMapper;

    @RequestMapping("/get-by-sql")
    @Operation(summary = "使用 SQL 查询数据")
    @PreAuthorize("@ss.hasPermission('report:go-view-data:get-by-sql')")
    public CommonResult<GoViewDataRespVO> getDataBySQL(@Valid @RequestBody GoViewDataGetBySqlReqVO reqVO) {
        return success(goViewDataService.getDataBySQL(reqVO.getSql()));
    }

    @RequestMapping("/get-by-http")
    @Operation(summary = "使用 HTTP 查询数据", description = "这个只是示例接口，实际应该每个查询，都要写一个接口")
    @PreAuthorize("@ss.hasPermission('report:go-view-data:get-by-http')")
    public CommonResult<GoViewDataRespVO> getDataByHttp(
            @RequestParam(required = false) Map<String, String> params,
            @RequestBody(required = false) String body) { // params、body 按照需要去接收，这里仅仅是示例
        GoViewDataRespVO respVO = new GoViewDataRespVO();
        // 1. 数据维度
        respVO.setDimensions(Arrays.asList("日期", "PV", "UV")); // PV 是每天访问次数；UV 是每天访问人数
        // 2. 明细数据列表
        // 目前通过随机的方式生成。一般来说，这里你可以写逻辑来实现数据的返回
        respVO.setSource(new LinkedList<>());
        for (int i = 1; i <= 12; i++) {
            String date = "2021-" + (i < 10 ? "0" + i : i);
            Integer pv = RandomUtil.randomInt(1000, 10000);
            Integer uv = RandomUtil.randomInt(100, 1000);
            respVO.getSource().add(MapUtil.<String, Object>builder().put("日期", date)
                    .put("PV", pv).put("UV", uv).build());
        }
        return success(respVO);
    }



    @RequestMapping("/blog-view-rank")
    @Operation(summary = "获取博客浏览量排行数据")
//    @PreAuthorize("@ss.hasPermission('report:go-view-data:get-by-http')")
    public CommonResult<Map<String, Object>> getBlogViewRank() {
        // 创建返回结果
        Map<String, Object> result = new HashMap<>();

        // 1. 设置维度
        result.put("dimensions", Arrays.asList("name", "value"));

        // 2. 创建查询条件
        LambdaQueryWrapperX<MyPostDO> queryWrapper = new LambdaQueryWrapperX<MyPostDO>()
                .eq(MyPostDO::getStatus, 2)  // 状态为已发布
                .eq(MyPostDO::getVisibility, 0)  // 可见性为公开
                .orderByDesc(MyPostDO::getViewCount)  // 按浏览量降序
                .last("LIMIT 12");  // 限制返回12条记录

        // 3. 查询数据库
        List<MyPostDO> posts = myPostMapper.selectList(queryWrapper);

        // 4. 转换为前端所需格式
        List<Map<String, Object>> source = new ArrayList<>();
        for (MyPostDO post : posts) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", post.getTitle());
            item.put("value", post.getViewCount());
            source.add(item);
        }

        result.put("source", source);

        return success(result);
    }

    @RequestMapping("/blog-like-rank")
    @Operation(summary = "获取博客点赞量排行数据")
//    @PreAuthorize("@ss.hasPermission('report:go-view-data:get-by-http')")
    public CommonResult<List<List<String>>> getBlogLikeRank() {
        // 创建查询条件
        LambdaQueryWrapper<MyPostDO> queryWrapper = new LambdaQueryWrapper<MyPostDO>()
                .eq(MyPostDO::getStatus, 2)  // 状态为已发布
                .eq(MyPostDO::getVisibility, 0)  // 可见性为公开
                .gt(MyPostDO::getLikeCount, 0)  // 点赞数大于0
                .orderByDesc(MyPostDO::getLikeCount)  // 按点赞数降序
                .last("LIMIT 10");  // 限制返回10条记录

        // 查询数据库
        List<MyPostDO> posts = myPostMapper.selectList(queryWrapper);

        // 转换为前端所需格式
        List<List<String>> result = new ArrayList<>();

        // 添加表头
        result.add(Arrays.asList("排名", "博客标题", "点赞数"));

        // 添加数据行
        int rank = 1;
        for (MyPostDO post : posts) {
            List<String> row = Arrays.asList(
                    String.valueOf(rank++),
                    post.getTitle(),
                    String.valueOf(post.getLikeCount())
            );
            result.add(row);
        }

        return success(result);
    }

    @RequestMapping("/blog-category-radar")
    @Operation(summary = "获取不同分类下博客数量雷达图数据")
//    @PreAuthorize("@ss.hasPermission('report:go-view-data:get-by-http')")
    public CommonResult<Map<String, Object>> getBlogCategoryRadar() {
        Map<String, Object> result = new HashMap<>();

        // 1. 获取所有分类
        List<CategoryDO> categories = categoryMapper.selectList(
                new LambdaQueryWrapperX<CategoryDO>()
                        .eq(CategoryDO::getParentId, 0) // 只取一级分类
                        .orderByDesc(CategoryDO::getId)
                        .last("LIMIT 6")); // 最多取6个分类，适合雷达图展示

        // 2. 构建雷达图指标
        List<Map<String, Object>> radarIndicator = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();

        for (CategoryDO category : categories) {
            categoryIds.add(category.getId());
            // 先用0作为最大值，后面会更新
            radarIndicator.add(createIndicator(category.getName(), 0));
        }

        // 3. 获取今年和去年的数据
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // 3.1 获取今年的数据
        Map<Long, Integer> thisYearData = getCategoryBlogCount(categoryIds, currentYear);

        // 3.2 获取去年的数据
        Map<Long, Integer> lastYearData = getCategoryBlogCount(categoryIds, currentYear - 1);

        // 4. 更新雷达图指标的最大值
        for (int i = 0; i < categories.size(); i++) {
            Long categoryId = categories.get(i).getId();
            int thisYearCount = thisYearData.getOrDefault(categoryId, 0);
            int lastYearCount = lastYearData.getOrDefault(categoryId, 0);
            // 设置max为两年中的最大值，并增加20%的余量
            int max = Math.max(thisYearCount, lastYearCount);
            max = (int) (max * 1.2);
            radarIndicator.get(i).put("max", max);
        }

        // 5. 构建系列数据
        List<Map<String, Object>> seriesData = new ArrayList<>();

        // 5.1 添加今年数据
        Map<String, Object> thisYear = new HashMap<>();
        thisYear.put("name", currentYear + "年");
        thisYear.put("value", categoryIds.stream()
                .map(id -> thisYearData.getOrDefault(id, 0))
                .collect(Collectors.toList()));
        seriesData.add(thisYear);

        // 5.2 添加去年数据
        Map<String, Object> lastYear = new HashMap<>();
        lastYear.put("name", (currentYear - 1) + "年");
        lastYear.put("value", categoryIds.stream()
                .map(id -> lastYearData.getOrDefault(id, 0))
                .collect(Collectors.toList()));
        seriesData.add(lastYear);

        result.put("radarIndicator", radarIndicator);
        result.put("seriesData", seriesData);

        return success(result);
    }

    /**
     * 获取指定年份各分类下的博客数量
     */
    private Map<Long, Integer> getCategoryBlogCount(List<Long> categoryIds, int year) {
        if (categoryIds.isEmpty()) {
            return new HashMap<>();
        }

        // 构建年份的起止时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        Date startTime = calendar.getTime();
        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        Date endTime = calendar.getTime();

        // 查询条件：已发布的博客
        LambdaQueryWrapper<MyPostDO> postWrapper = new LambdaQueryWrapper<MyPostDO>()
                .eq(MyPostDO::getStatus, 2)  // 已发布
                .eq(MyPostDO::getVisibility, 0)  // 公开
                .between(MyPostDO::getCreateTime, startTime, endTime);

        // 获取所有符合条件的博客ID
        List<MyPostDO> posts = myPostMapper.selectList(postWrapper);
        Set<Long> postIds = posts.stream()
                .map(MyPostDO::getId)
                .collect(Collectors.toSet());

        if (postIds.isEmpty()) {
            return new HashMap<>();
        }

        // 查询博客分类关联
        LambdaQueryWrapperX<PostCategoryDO> categoryWrapper = new LambdaQueryWrapperX<PostCategoryDO>()
                .in(PostCategoryDO::getPostId, postIds)
                .in(PostCategoryDO::getCategoryId, categoryIds);

        List<PostCategoryDO> postCategories = postCategoryMapper.selectList(categoryWrapper);

        // 统计每个分类下的博客数量
        return postCategories.stream()
                .collect(Collectors.groupingBy(
                        PostCategoryDO::getCategoryId,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));
    }

    @RequestMapping("/blog-list-table")
    @Operation(summary = "获取博客列表表格数据")
//    @PreAuthorize("@ss.hasPermission('report:go-view-data:get-by-http')")
    public CommonResult<Map<String, Object>> getBlogListTable() {
        Map<String, Object> result = new HashMap<>();

        // 1. 构建表格维度
        List<Map<String, Object>> dimensions = new ArrayList<>();
        dimensions.add(createDimension("博客标题", "title", "center"));
        dimensions.add(createDimension("浏览量", "viewCount", "center"));
        dimensions.add(createDimension("点赞数", "likeCount", "center"));
        dimensions.add(createDimension("创建时间", "createTime", "center"));
        dimensions.add(createDimension("状态", "status", "center"));

        // 2. 创建查询条件
        LambdaQueryWrapperX<MyPostDO> queryWrapper = new LambdaQueryWrapperX<MyPostDO>()
                .eq(MyPostDO::getVisibility, 0)  // 可见性为公开
                .orderByDesc(MyPostDO::getCreateTime)  // 按创建时间降序
                .last("LIMIT 10");  // 限制返回10条记录

        // 3. 查询数据库
        List<MyPostDO> posts = myPostMapper.selectList(queryWrapper);

        // 4. 构建表格数据
        List<Map<String, Object>> source = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            MyPostDO post = posts.get(i);
            Map<String, Object> row = new HashMap<>();
            row.put("key", i);
            row.put("title", post.getTitle());
            row.put("viewCount", post.getViewCount());
            row.put("likeCount", post.getLikeCount());
            row.put("createTime", post.getCreateTime());
            row.put("status", convertStatus(post.getStatus())); // 转换状态显示
            source.add(row);
        }

        result.put("dimensions", dimensions);
        result.put("source", source);

        return success(result);
    }

    /**
     * 创建排行项
     *
     * @param name  博客标题
     * @param value 浏览量
     * @return 排行项
     */
    private Map<String, Object> createRankItem(String name, Integer value) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }

    /**
     * 创建雷达图指标项
     */
    private Map<String, Object> createIndicator(String name, Integer max) {
        Map<String, Object> indicator = new HashMap<>();
        indicator.put("name", name);
        indicator.put("max", max);
        return indicator;
    }

    /**
     * 创建表格维度项
     */
    private Map<String, Object> createDimension(String title, String key, String align) {
        Map<String, Object> dimension = new HashMap<>();
        dimension.put("title", title);
        dimension.put("key", key);
        dimension.put("align", align);
        return dimension;
    }

    /**
     * 转换博客状态为文字描述
     */
    private String convertStatus(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0:
                return "草稿";
            case 1:
                return "待审核";
            case 2:
                return "已发布";
            case 3:
                return "已下架";
            default:
                return "未知";
        }
    }

    @RequestMapping("/blog-tag-cloud")
    @Operation(summary = "获取博客标签云数据")
//    @PreAuthorize("@ss.hasPermission('report:go-view-data:get-by-http')")
    public CommonResult<List<Map<String, Object>>> getBlogTagCloud() {
        // 1. 查询所有已发布的博客ID
        List<Long> publishedPostIds = myPostMapper.selectList(
                        new LambdaQueryWrapperX<MyPostDO>()
                                .eq(MyPostDO::getStatus, 2)  // 已发布
                                .eq(MyPostDO::getVisibility, 0)  // 公开
                                .select(MyPostDO::getId))
                .stream()
                .map(MyPostDO::getId)
                .collect(Collectors.toList());

        if (publishedPostIds.isEmpty()) {
            return success(new ArrayList<>());
        }

        // 2. 获取标签使用频率映射
        Map<Long, Integer> tagFrequencyMap = postTagMapper.selectList(
                        new LambdaQueryWrapperX<PostTagDO>()
                                .in(PostTagDO::getPostId, publishedPostIds))
                .stream()
                .collect(Collectors.groupingBy(
                        PostTagDO::getTagId,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                count -> count.intValue() * 100 // 将使用次数转换为权重值
                        )));

        if (tagFrequencyMap.isEmpty()) {
            return success(new ArrayList<>());
        }

        // 3. 找出最大使用频率
        Integer maxFrequency = Collections.max(tagFrequencyMap.values());

        // 4. 构建标签云数据
        List<Map<String, Object>> tagCloudData = tagFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed()) // 按使用频率降序
                .limit(20) // 最多取20个标签
                .map(entry -> {
                    // 获取标签信息
                    TagDO tag = tagMapper.selectById(entry.getKey());
                    if (tag == null) {
                        return null;
                    }

                    // 构建标签项
                    Map<String, Object> tagItem = new HashMap<>();
                    tagItem.put("name", tag.getName());
                    tagItem.put("value", entry.getValue());

                    // 为使用最多的标签添加特殊样式
                    if (entry.getValue().equals(maxFrequency)) {
                        // 添加文字样式
                        Map<String, Object> textStyle = new HashMap<>();
                        textStyle.put("color", "#78fbb2");
                        tagItem.put("textStyle", textStyle);
                        // 添加强调样式
                        Map<String, Object> emphasis = new HashMap<>();
                        Map<String, Object> emphasisTextStyle = new HashMap<>();
                        emphasisTextStyle.put("color", "red");
                        emphasis.put("textStyle", emphasisTextStyle);
                        tagItem.put("emphasis", emphasis);
                    }

                    return tagItem;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return success(tagCloudData);
    }
}
