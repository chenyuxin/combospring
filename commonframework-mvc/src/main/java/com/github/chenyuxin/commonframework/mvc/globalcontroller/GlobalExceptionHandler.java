package com.github.chenyuxin.commonframework.mvc.globalcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局controller的异常捕获处理
 */
@Slf4j
@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * 测试环境返回实际报错的消息，正式环境封装消息，返回固定的错误信息
	 */
	@Value("${spring.profiles.active}")
    private String profile;
	
	private static String errorMsg = "系统出现错误，请重试或联系平台管理员。";
	
    @ExceptionHandler
    public ResponseEntity<?> handleException(Throwable e){ // 处理方法参数的异常类型
    	log.error("GlobalExceptionHandler捕获异常：", e);
    	Map<String, Object> responseMsg = new HashMap<String, Object>();
		responseMsg.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		responseMsg.put("message", "test".equalsIgnoreCase(profile)||"local".equalsIgnoreCase(profile) ? e.getMessage() : errorMsg);
        return ResponseEntity.internalServerError().body(responseMsg);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationException(ValidationException e){
    	log.warn("GlobalExceptionHandler捕获异常ValidationException：", e);
    	Map<String, Object> responseMsg = new HashMap<String, Object>();
		responseMsg.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
		responseMsg.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(responseMsg);
    }
 
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e){
    	log.error("GlobalExceptionHandler捕获异常RuntimeException：", e);
    	Map<String, Object> responseMsg = new HashMap<String, Object>();
		responseMsg.put("status", HttpStatus.NOT_IMPLEMENTED.value());
		responseMsg.put("message", "test".equalsIgnoreCase(profile)||"local".equalsIgnoreCase(profile) ? e.getMessage() : errorMsg);
        return ResponseEntity.internalServerError().body(responseMsg);
    }
    
    
 
    
}    