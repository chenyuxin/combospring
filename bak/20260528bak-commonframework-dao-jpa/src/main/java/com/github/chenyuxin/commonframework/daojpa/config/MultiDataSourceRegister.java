package com.github.chenyuxin.commonframework.daojpa.config;

import java.util.Map;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.config.druid.DruidDataSourceWrapper;

/**
 * 自动注入多数据源
 * 读取 MultiDataSourceProperties 配置，动态注册 DataSource, EntityManager,
 * TransactionManager
 */
@Configuration
public class MultiDataSourceRegister {
    private static final Logger log = LoggerFactory.getLogger(MultiDataSourceRegister.class);

    @Autowired
    private MultiDataSourceProperties properties;

    @Autowired
    private GenericApplicationContext context;

    @PostConstruct
    public void registerDataSources() {
        if (properties == null || properties.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 如果值是Map，说明是嵌套的数据源配置 (e.g. spring.datasource.mydata.url)
            // 排除非数据源配置项
            if (value instanceof Map && !isReservedKey(key)) {
                log.info("Found multi-datasource config via Properties: {}", key);
                registerDataSourceBeans(key, (Map<?, ?>) value);
            }
        }
    }

    private boolean isReservedKey(String key) {
        // Exclude standard keys and druid specific config
        return "druid".equals(key) || "type".equals(key) || "driver-class-name".equals(key)
                || "url".equals(key) || "username".equals(key) || "password".equals(key)
                || "hikari".equals(key) || "tomcat".equals(key) || "dbcp2".equals(key);
    }

    private void registerDataSourceBeans(String dataSourceName, Map<?, ?> dsProperties) {
        // 1. Register DataSource (DruidDataSourceWrapper)
        if (!context.containsBeanDefinition(dataSourceName)) {
            BeanDefinitionBuilder dataSourceBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(DruidDataSourceWrapper.class);

            // Bind properties explicitly from the map
            dataSourceBuilder.addPropertyValue("url", dsProperties.get("url"));
            dataSourceBuilder.addPropertyValue("username", dsProperties.get("username"));
            dataSourceBuilder.addPropertyValue("password", dsProperties.get("password"));
            dataSourceBuilder.addPropertyValue("driverClassName", dsProperties.get("driver-class-name"));
            // Druid specific: initial-size, max-active etc can be added if needed,
            // but simple binding matches previous logic.

            context.registerBeanDefinition(dataSourceName, dataSourceBuilder.getBeanDefinition());
            log.info("Registered DataSource bean: {}", dataSourceName);
        }

        // 2. Register EntityManagerFactory
        String emfBeanName = dataSourceName + DaoConst.EntityManagerFactory;
        String emBeanName = dataSourceName + DaoConst.EntityManager;
        String tmBeanName = dataSourceName + DaoConst.TransactionManager;

        if (!context.containsBeanDefinition(emfBeanName)) {
            BeanDefinitionBuilder emfBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(LocalContainerEntityManagerFactoryBean.class);
            emfBuilder.addPropertyReference("dataSource", dataSourceName);
            emfBuilder.addPropertyValue("packagesToScan", "com.github.chenyuxin");
            emfBuilder.addPropertyValue("jpaVendorAdapter", new HibernateJpaVendorAdapter());

            context.registerBeanDefinition(emfBeanName, emfBuilder.getBeanDefinition());
            log.info("Registered EntityManagerFactory bean: {}", emfBeanName);
        }

        // 3. Register EntityManager (SharedEntityManager properties)
        if (!context.containsBeanDefinition(emBeanName)) {
            BeanDefinitionBuilder emBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(SharedEntityManagerCreator.class);
            emBuilder.setFactoryMethod("createSharedEntityManager");
            emBuilder.addConstructorArgReference(emfBeanName);

            context.registerBeanDefinition(emBeanName, emBuilder.getBeanDefinition());
            log.info("Registered EntityManager bean: {}", emBeanName);
        }

        // 4. Register TransactionManager
        if (!context.containsBeanDefinition(tmBeanName)) {
            BeanDefinitionBuilder tmBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(DataSourceTransactionManager.class);
            tmBuilder.addPropertyReference("dataSource", dataSourceName);
            context.registerBeanDefinition(tmBeanName, tmBuilder.getBeanDefinition());
            log.info("Registered TransactionManager bean: {}", tmBeanName);
        }
    }
}
