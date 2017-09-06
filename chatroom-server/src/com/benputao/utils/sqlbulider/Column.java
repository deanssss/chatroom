package com.benputao.utils.sqlbulider;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ���ݱ���ֶα�ʶע��
 * valueΪ�ֶ�����
 * @author benputao
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Column {
	String value();
}
