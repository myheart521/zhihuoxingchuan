package com.example.project.module.system.service.dict;

import cn.hutool.core.bean.BeanUtil;
import com.example.project.framework.common.biz.system.dict.DictDataCommonApi;
import com.example.project.framework.common.biz.system.dict.dto.DictDataRespDTO;
import com.example.project.module.system.dal.dataobject.dict.DictDataDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DictDataCommonApiImpl implements DictDataCommonApi {

    @Autowired
    private DictDataService dictDataService;
    @Override
    public List<DictDataRespDTO> getDictDataList(String dictType) {
        List<DictDataDO> dictDataList = dictDataService.getDictDataList(0, dictType);
        return BeanUtil.copyToList(dictDataList, DictDataRespDTO.class);
    }
}
