package com.github.chenyuxin.commonframework.mvc.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages="com.**.controller,com.**.web,com.**.rest,com.**.*controller",
includeFilters={@Filter(type=FilterType.ANNOTATION,classes={Controller.class,ControllerAdvice.class,RestController.class})},
excludeFilters={@Filter(type=FilterType.ANNOTATION,classes={Service.class,Repository.class})}
)
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer{
	
	@Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
            //.allowedOrigins("http://loacalhost:3000")
        	.allowedOriginPatterns("*")
            //.allowedMethods("GET", "POST")
            //.allowedHeaders("Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With")
            //.exposedHeaders("header1", "header2")
            .allowCredentials(true).maxAge(3600)
        	;
        // Add more mappings...
    }
	
	/**
	 * 处理静态资源
	 */
	@Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")
            .addResourceLocations("/WEB-INF/static/");
		//registry.addResourceHandler("/**").addResourceLocations("classpath*:/META-INF/resources/");
		//registry.addResourceHandler("/**").addResourceLocations("file:C:/Users/DELL-AB/Desktop/test/resources");
    }
	
	
	/**
	<!-- 静态资源访问 -->
  	<mvc:default-servlet-handler/>
  	*/
  	@Override
    public void configureDefaultServletHandling(@NonNull DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
        //configurer.enable("dispatcherServlet");
    }
	
	@Bean(name="htmlViewResolver")
	public InternalResourceViewResolver htmlViewResolver(){
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setOrder(0);
		internalResourceViewResolver.setPrefix("/WEB-INF/");
		internalResourceViewResolver.setContentType("text/html;charset=UTF-8");
		internalResourceViewResolver.setSuffix(".html");
		internalResourceViewResolver.setViewNames("index");
		return internalResourceViewResolver;
	}
	
	@Bean(name="jspViewResolver")
	public InternalResourceViewResolver jspViewResolver(){
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setOrder(1);
		internalResourceViewResolver.setPrefix("/WEB-INF/admin/");
		internalResourceViewResolver.setContentType("text/html;charset=UTF-8");
		internalResourceViewResolver.setSuffix(".jsp");
		return internalResourceViewResolver;
	}
	
	/**
	 * 视图解析器
	 */
	@Override
    public void configureViewResolvers(@NonNull ViewResolverRegistry registry) {
		registry.viewResolver(this.htmlViewResolver());
		registry.viewResolver(this.jspViewResolver());
    }
	
    
}


