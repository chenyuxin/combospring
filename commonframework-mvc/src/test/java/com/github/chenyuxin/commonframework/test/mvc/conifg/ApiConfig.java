package com.github.chenyuxin.commonframework.test.mvc.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class ApiConfig {
	
	/**
	 * Knife4j,前端api接口
	 * @return
	 */
	@Bean
    public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("接口API")
						.version("1.0")
						.description("这里是描述")
						.termsOfService("https://github.com/chenyuxin/combospring")
						.contact(new Contact().name("test").url("https://github.com/chenyuxin/combospring").email("wosisingle@163.com"))
				);
    }
    
}


