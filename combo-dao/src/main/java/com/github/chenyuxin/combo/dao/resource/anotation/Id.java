package com.github.chenyuxin.combo.dao.resource.anotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 判断字段是否主键<br>
 * commmonDao中使用update,saveOrUpdate时必须添加该注解<br>
 * saveOrUpdate在mysql中使用时，对应字段必须建立为主键，高并发主从分离mysql数据库不建议使用saveOrUpdate，主从同步的原因<br>
 * 在postgresql中使用时，对应字段必须建立为主键或唯一索引
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Id {
}
