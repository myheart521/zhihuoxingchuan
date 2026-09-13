package com.example.project.framework.tracer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * BizTracer配置类
 *

 */
@ConfigurationProperties("project.tracer")
@Data
public class TracerProperties {
}
