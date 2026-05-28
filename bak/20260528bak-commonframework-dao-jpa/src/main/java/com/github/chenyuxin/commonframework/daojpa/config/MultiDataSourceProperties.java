package com.github.chenyuxin.commonframework.daojpa.config;

import java.util.LinkedHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Capture all spring.datasource.* properties.
 * Extends LinkedHashMap to dynamically capture nested properties (like ds2,
 * mydata, etc).
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class MultiDataSourceProperties extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    // No extra code needed, map logic handles dynamic properties.
}
