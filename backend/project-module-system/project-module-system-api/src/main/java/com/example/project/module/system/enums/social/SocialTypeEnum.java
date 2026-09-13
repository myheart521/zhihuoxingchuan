package com.example.project.module.system.enums.social;

import cn.hutool.core.util.ArrayUtil;
import com.example.project.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 社交平台的类型枚举
 *

 */
@Getter
@AllArgsConstructor
public enum SocialTypeEnum implements ArrayValuable<Integer> {

    /**
     * Gitee
     *
     * @see <a href="https://gitee.com/api/v5/oauth_doc#/">接入文档</a>
     */
    GITEE(10, "GITEE"),
    /**
     * 钉钉
     *
     * @see <a href="https://example.invalid/resource">接入文档</a>
     */
    DINGTALK(20, "DINGTALK"),

    /**
     * 企业微信
     *
     * @see <a href="https://example.invalid/resource">接入文档</a>
     */
    WECHAT_ENTERPRISE(30, "WECHAT_ENTERPRISE"),
    /**
     * 微信公众平台 - 移动端 H5
     *
     * @see <a href="https://example.invalid/resource">接入文档</a>
     */
    WECHAT_MP(31, "WECHAT_MP"),
    /**
     * 微信开放平台 - 网站应用 PC 端扫码授权登录
     *
     * @see <a href="https://example.invalid/resource">接入文档</a>
     */
    WECHAT_OPEN(32, "WECHAT_OPEN"),
    /**
     * 微信小程序
     *
     * @see <a href="https://example.invalid/resource">接入文档</a>
     */
    WECHAT_MINI_APP(34, "WECHAT_MINI_APP"),
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(SocialTypeEnum::getType).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 类型的标识
     */
    private final String source;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static SocialTypeEnum valueOfType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
