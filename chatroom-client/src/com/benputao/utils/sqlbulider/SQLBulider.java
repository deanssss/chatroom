package com.benputao.utils.sqlbulider;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SQL��乹������<br>
 * <br>��ʹ��<br>
 * {@link Table} @Table(tablename) <br>
 * {@link Column} @Column(columnname)
 * <br>������ѯʵ���������ݱ�ı������ֶΣ�<br>
 * �ٽ��ò�ѯʵ�������sql��乹�췽���й�����Ӧ�Ĳ�ѯ��䡣
 * @author benputao
 */
public class SQLBulider {
	/**
	 * ��ѯ��乹�췽��
	 * @param o ��ѯʵ�����,�ö�������@Table @Columnע��
	 * @param columns ��ʾ�ֶ��б�
	 * @return ��Ӧ��sql���
	 */
	public static String buildQuery(Object o,List<String>columns){
		StringBuilder builder=new StringBuilder();
		Class<? extends Object> c=o.getClass();
		if (!c.isAnnotationPresent(Table.class)) {
			System.err.println("��ʹ��@Tableע��ע����");
			return null;
		}
		//��ȡ����
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
		//��ȡ�������ֶ�
		Field[]fields=c.getDeclaredFields();
		for(Field field:fields){
			if(!(field.isAnnotationPresent(Column.class))){
				continue;
			}
			//��ȡ������
			Column cc= field.getAnnotation(Column.class);
			String column=cc.value();
			//��ȡ����ֵ
			String fieldName=field.getName();
			String methodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			Method method;
			try {
				method = c.getMethod(methodName);
				Object value=method.invoke(o);
				if(value!=null){
					//ƴװSQL
					builder.append(" and "+column+"=");
					if(value instanceof String||value instanceof CharSequence){
						builder.append("'"+value+"'");
					}else if (value instanceof Date) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						builder.append("'"+dateFormat.format(value)+"'");
					}else if(value instanceof Enum){	//��ö�ٵĴ���
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
			System.err.println("��ʹ��@Tableע��ע����");
			return null;
		}
		//��ȡ����
		Table t=c.getAnnotation(Table.class);
		String table=t.value();
		//����sql
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
				}else if(value instanceof Enum){	//��ö�ٵĴ���
					builder.append("'"+value+"',");
				}else {
					builder.append(value+",");
				}
			}
		}
		builder.replace(builder.length()-1, builder.length(), " ");
		builder.append("where 1=1");
		//��ȡ��ѯ�ֶ�
		Field[]fields=c.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column cc=field.getAnnotation(Column.class);
			String column=cc.value();		//��ȡ������
			String fieldName=field.getName();	//��ȡ�ֶ���
			String methodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			Method method;
			try {
				method=c.getMethod(methodName);
				Object value=method.invoke(o);
				if(value!=null){
					//ƴװSQL
					builder.append(" and "+column+"=");
					if(value instanceof String||value instanceof CharSequence){
						builder.append("'"+value+"'");
					}else if (value instanceof Date) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						builder.append("'"+dateFormat.format(value)+"'");
					}else if(value instanceof Enum){	//��ö�ٵĴ���
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
			System.err.println("��ʹ��@Tableע��ע����");
			return null;
		}
		//��ȡ����
		Table t=(Table)c.getAnnotation(Table.class);
		String table=t.value();
		//����sql
		builder.append("insert into "+table);
		
		StringBuilder insfield=new StringBuilder();
		StringBuilder values=new StringBuilder();
		insfield.append("(");
		values.append(" values(");
		//��ȡ�ֶμ�ֵ
		Field[]fields=c.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column cc=field.getAnnotation(Column.class);
			String column=cc.value();		//��ȡ������
			String fieldName=field.getName();	//��ȡ�ֶ���
			String methodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			Method method;
			try {
				method=c.getMethod(methodName);
				Object value=method.invoke(o);
				if(value!=null){
					insfield.append(column+",");
					//ƴװSQL
					if(value instanceof String||value instanceof CharSequence){
						values.append("'"+value+"',");
					}else if (value instanceof Date) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						values.append("'"+dateFormat.format(value)+"',");
					}else if(value instanceof Enum){	//��ö�ٵĴ���
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
