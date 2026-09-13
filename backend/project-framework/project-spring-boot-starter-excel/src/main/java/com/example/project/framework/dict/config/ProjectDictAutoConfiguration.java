package com.example.project.framework.dict.config;

import com.example.project.framework.dict.core.DictFrameworkUtils;
import com.example.project.framework.common.biz.system.dict.DictDataCommonApi;

import com.example.project.module.system.api.dict.DictDataApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ProjectDictAutoConfiguration {

    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public DictFrameworkUtils dictUtils(DictDataCommonApi dictDataApi) {
        DictFrameworkUtils.init(dictDataApi);
        return new DictFrameworkUtils();
    }

}
