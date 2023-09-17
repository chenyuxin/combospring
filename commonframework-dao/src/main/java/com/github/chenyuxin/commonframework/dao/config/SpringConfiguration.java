package com.github.chenyuxin.commonframework.dao.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.github.chenyuxin.commonframework.dao.common.DaoConst;

import org.springframework.context.annotation.ComponentScan.Filter;

/**
 * 使用配置类配置spring;
 * 使用springboot启动时自动装配;
 */
@Configuration
@AutoConfigureBefore(PropertyPlaceholderAutoConfiguration.class)
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@ComponentScan(basePackages="com.github.chenyuxin",
excludeFilters={@Filter(type=FilterType.ANNOTATION,
		classes={Controller.class,ControllerAdvice.class,RestController.class,EnableWebMvc.class})}
)
@MapperScan(annotationClass = Mapper.class,basePackages = "com.github.chenyuxin")
@Import({DruidDataSourceAutoConfigure.class,DaoConfResource.class})//引入druid连接池
public class SpringConfiguration {
	
	/**
	 * 使用NamedParameterJdbcTemplate参数命名springJdbc为commonDao的jdbc
	 * @param dataSource
	 * @return
	 */
	@Bean("NamedParameterJdbcTemplate")
	public static NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource){
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	/**
	 * Mybatis
	 * @param dataSource
	 * @return
	 * @throws IOException
	 */
	@Bean("sqlSessionFactory")
	public static SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		//注入数据库连接池
		sqlSessionFactoryBean.setDataSource(dataSource);
		
		//配置MyBaties全局配置文件:mybatis-config.xml
//		PathMatchingResourcePatternResolver prpr = new PathMatchingResourcePatternResolver();
//		Resource resource = prpr.getResource("classpath:mybatis-config.xml");
//		sqlSessionFactoryBean.setConfigLocation(resource);
		
		//扫描entity包 使用别名
		sqlSessionFactoryBean.setTypeAliasesPackage("com.github.chenyuxin");
		
		//扫描sql配置文件:mapper需要的xml文件
		PathMatchingResourcePatternResolver prpr2 = new PathMatchingResourcePatternResolver();
    	Resource[] resourcePropertiesFile = prpr2.getResources("classpath*:/mapper/*.xml");
		sqlSessionFactoryBean.setMapperLocations(resourcePropertiesFile);
		
		return sqlSessionFactoryBean;
	}
	
	/**
	 * 对默认数据源创建事务管理器
	 * @param dataSource
	 * @return
	 */
	//@Bean("dataSourceTransactionManager")
	@Bean(DaoConst.defaultDataSourceName + DaoConst.TransactionManager)
	public static TransactionManager transactionManager(DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource); // 设置数据源
        return dataSourceTransactionManager;
	}
	
	/**
	 * 创建资源文件解析器(读配置文件properties)
	 * @throws IOException 
	//注解大类的@PropertySource无法使用通配符,只能定义file路径或者classpath:
	//@PropertySource(value="classpath*:/properties/*.properties",encoding="UTF-8",ignoreResourceNotFound=false)
	//@PropertySource(value="WEB-INF/classes/properties/applicationJdbc.properties",encoding="UTF-8",ignoreResourceNotFound=false)
	 */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException{
    	PathMatchingResourcePatternResolver prpr = new PathMatchingResourcePatternResolver();
    	
    	Resource[] resourcePropertiesFile = prpr.getResources("classpath*:/properties/*.properties");
    	Resource[] resource = prpr.getResources("classpath*:*.properties");
    	
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		pspc.setFileEncoding("UTF-8");
		//pspc.setIgnoreUnresolvablePlaceholders(true);//忽略读取错误配置
    	pspc.setLocations(ArrayUtils.addAll(resourcePropertiesFile, resource));
    	return pspc;
    }

}