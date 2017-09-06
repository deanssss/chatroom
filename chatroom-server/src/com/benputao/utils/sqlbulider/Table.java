package com.benputao.utils.sqlbulider;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 数据表表名注释标识<br>
 * value为对应数据表的名字
 * @author benputao
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Table {
	String value();
}
