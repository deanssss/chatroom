package com.benputao.utils.sqlbulider;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 数据表的字段标识注释
 * value为字段名称
 * @author benputao
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Column {
	String value();
}
