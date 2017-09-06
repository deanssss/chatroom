package com.benputao.utils.sqlbulider;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SQL语句构建工具<br>
 * <br>先使用<br>
 * {@link Table} @Table(tablename) <br>
 * {@link Column} @Column(columnname)
 * <br>关联查询实体类与数据表的表名与字段，<br>
 * 再将该查询实体对象传入sql语句构造方法中构造相应的查询语句。
 * @author benputao
 */
public class SQLBulider {
	/**
	 * 查询语句构造方法
	 * @param o 查询实体对象,该对象需由@Table @Column注释
	 * @param columns 显示字段列表
	 * @return 相应的sql语句
	 */
	public static String buildQuery(Object o,List<String>columns){
		StringBuilder builder=new StringBuilder();
		Class<? extends Object> c=o.getClass();
		if (!c.isAnnotationPresent(Table.class)) {
			System.err.println("请使用@Table注解注释类");
			return null;
		}
		//获取表名
		Table t=(Table)c.getAnnotation(Table.class);
		String table=t.value();
		
		builder.append("select ");
		if(columns==null||columns.isEmpty()){
			builder.append("*");
		}else {
			for (String col : columns) {
				builder.append(col+",");
			}
			builder.replace(builder.length()-1, builder.length(), " ");
		}
		builder.append("from "+table+" where 1=1");
		//获取类所有字段
		Field[]fields=c.getDeclaredFields();
		for(Field field:fields){
			if(!(field.isAnnotationPresent(Column.class))){
				continue;
			}
			//获取属性名
			Column cc= field.getAnnotation(Column.class);
			String column=cc.value();
			//获取属性值
			String fieldName=field.getName();
			String methodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			Method method;
			try {
				method = c.getMethod(methodName);
				Object value=method.invoke(o);
				if(value!=null){
					//拼装SQL
					builder.append(" and "+column+"=");
					if(value instanceof String||value instanceof CharSequence){
						builder.append("'"+value+"'");
					}else if (value instanceof Date) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						builder.append("'"+dateFormat.format(value)+"'");
					}else if(value instanceof Enum){	//对枚举的处理
						builder.append("'"+value+"'");
					}else {
						builder.append(value);
					}
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	public static String buildUpdate(Object o,Map<String, Object>values) {
		StringBuilder builder=new StringBuilder();
		Class<? extends Object>c=o.getClass();
		if (!c.isAnnotationPresent(Table.class)) {
			System.err.println("请使用@Table注解注释类");
			return null;
		}
		//获取表名
		Table t=c.getAnnotation(Table.class);
		String table=t.value();
		//构建sql
		builder.append("update "+table+" set ");
		for(Map.Entry<String, Object>entry:values.entrySet()){
			String column=entry.getKey();
			Object value=entry.getValue();
			if(value!=null){
				builder.append(column+"=");
				if(value instanceof String||value instanceof CharSequence){
					builder.append("'"+value+"',");
				}else if (value instanceof Date) {
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					builder.append("'"+dateFormat.format(value)+"',");
				}else if(value instanceof Enum){	//对枚举的处理
					builder.append("'"+value+"',");
				}else {
					builder.append(value+",");
				}
			}
		}
		builder.replace(builder.length()-1, builder.length(), " ");
		builder.append("where 1=1");
		//获取查询字段
		Field[]fields=c.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column cc=field.getAnnotation(Column.class);
			String column=cc.value();		//获取属性名
			String fieldName=field.getName();	//获取字段名
			String methodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			Method method;
			try {
				method=c.getMethod(methodName);
				Object value=method.invoke(o);
				if(value!=null){
					//拼装SQL
					builder.append(" and "+column+"=");
					if(value instanceof String||value instanceof CharSequence){
						builder.append("'"+value+"'");
					}else if (value instanceof Date) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						builder.append("'"+dateFormat.format(value)+"'");
					}else if(value instanceof Enum){	//对枚举的处理
						builder.append("'"+value+"'");
					}else {
						builder.append(value);
					}
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}

	public static String buildInsert(Object o){
		StringBuilder builder=new StringBuilder();
		Class<? extends Object>c=o.getClass();
		if (!c.isAnnotationPresent(Table.class)) {
			System.err.println("请使用@Table注解注释类");
			return null;
		}
		//获取表名
		Table t=(Table)c.getAnnotation(Table.class);
		String table=t.value();
		//构造sql
		builder.append("insert into "+table);
		
		StringBuilder insfield=new StringBuilder();
		StringBuilder values=new StringBuilder();
		insfield.append("(");
		values.append(" values(");
		//获取字段及值
		Field[]fields=c.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column cc=field.getAnnotation(Column.class);
			String column=cc.value();		//获取属性名
			String fieldName=field.getName();	//获取字段名
			String methodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			Method method;
			try {
				method=c.getMethod(methodName);
				Object value=method.invoke(o);
				if(value!=null){
					insfield.append(column+",");
					//拼装SQL
					if(value instanceof String||value instanceof CharSequence){
						values.append("'"+value+"',");
					}else if (value instanceof Date) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						values.append("'"+dateFormat.format(value)+"',");
					}else if(value instanceof Enum){	//对枚举的处理
						values.append("'"+value+"',");
					}else {
						values.append(value+",");
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		values.replace(values.length()-1, values.length(), ")");
		insfield.replace(insfield.length()-1, insfield.length(), ")");
		builder.append(insfield.toString()+values.toString());
		
		return builder.toString();
	}
	
//	public static void main(String[] args) {
//		User user=new User("10000","1245464",null,new Date(),new Date(),User.State.OFFLINE);
//		System.out.println(SQLBulider.buildInsert(user));
//	}
}
