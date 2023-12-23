package com.github.chenyuxin.commonframework.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
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

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.JSONReader.Feature;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.github.chenyuxin.commonframework.base.util.CharsetUtil;


@Configuration
@ComponentScan(basePackages="com.github.chenyuxin.**.controller,com.github.chenyuxin.**.web,com.github.chenyuxin.**.rest,com.github.chenyuxin.**.*controller",
includeFilters={@Filter(type=FilterType.ANNOTATION,classes={Controller.class,ControllerAdvice.class,RestController.class})},
excludeFilters={@Filter(type=FilterType.ANNOTATION,classes={Service.class,Repository.class})}
)
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer{
	
	@Bean(name="fastJsonHttpMessageConverter")
	public FastJsonHttpMessageConverter httpMessageConverter(){
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<>(16);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		fastJsonConfig.setCharset(CharsetUtil.CHARSET_UTF_8);
		fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteMapNullValue,JSONWriter.Feature.WriteNulls);
		fastJsonConfig.setReaderFeatures(Feature.UseNativeObject);
		
        // 将配置设置给转换器并添加到HttpMessageConverter转换器列表中
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		return fastJsonHttpMessageConverter;
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(this.httpMessageConverter());
	}
	
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
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
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
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
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
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
    public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.viewResolver(this.htmlViewResolver());
		registry.viewResolver(this.jspViewResolver());
    }
	
    
}


