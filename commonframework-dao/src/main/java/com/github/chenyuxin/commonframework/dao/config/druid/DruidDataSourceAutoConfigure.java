package com.github.chenyuxin.commonframework.dao.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.chenyuxin.commonframework.dao.config.druid.properties.DruidStatProperties;
import com.github.chenyuxin.commonframework.dao.config.druid.stat.DruidFilterConfiguration;
import com.github.chenyuxin.commonframework.dao.config.druid.stat.DruidSpringAopConfiguration;
import com.github.chenyuxin.commonframework.dao.config.druid.stat.DruidStatViewServletConfiguration;
import com.github.chenyuxin.commonframework.dao.config.druid.stat.DruidWebStatFilterConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @author lihengming [89921218@qq.com]
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.type",
        havingValue = "com.alibaba.druid.pool.DruidDataSource",
        matchIfMissing = true)
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class})
@Import({DruidSpringAopConfiguration.class,
        DruidStatViewServletConfiguration.class,
        DruidWebStatFilterConfiguration.class,
        DruidFilterConfiguration.class})
public class DruidDataSourceAutoConfigure {
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceAutoConfigure.class);

    /**
     * Not setting initMethod of annotation {@code @Bean} is to avoid failure when inspecting
     * the bean definition at the build time. The {@link DruidDataSource#init()} will be called
     * at the end of {@link DruidDataSourceWrapper#afterPropertiesSet()}.
     *
     * @return druid data source wrapper
     */
    @Bean
    @ConditionalOnMissingBean({DruidDataSourceWrapper.class,
        DruidDataSource.class,
        DataSource.class})
    public DruidDataSourceWrapper dataSource() {
        LOGGER.info("Init DruidDataSource");
        return new DruidDataSourceWrapper();
    }
}
