package com.example.project.module.infra.dal.mysql.db;

import com.example.project.framework.mybatis.core.mapper.BaseMapperX;
import com.example.project.module.infra.dal.dataobject.db.DataSourceConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据源配置 Mapper
 *

 */
@Mapper
public interface DataSourceConfigMapper extends BaseMapperX<DataSourceConfigDO> {
}
