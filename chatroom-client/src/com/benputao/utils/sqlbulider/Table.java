package com.benputao.utils.sqlbulider;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ���ݱ����ע�ͱ�ʶ<br>
 * valueΪ��Ӧ���ݱ������
 * @author benputao
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Table {
	String value();
}
