package com.ober.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanToMapUtil {
	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param clazz
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 */
	public static Object convertMap(Class<?> clazz, Map<String, Object> map) {
		Object obj = null;
		try {
			// 获取类属性
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			obj = clazz.newInstance(); // 创建 JavaBean 对象
			// 给 JavaBean 对象的属性赋值
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propertyName = descriptor.getName();
				if (map.containsKey(propertyName)) {
					Object value = map.get(propertyName);
					Object[] args = new Object[1];
					args[0] = value;
					descriptor.getWriteMethod().invoke(obj, args);
				}
			}

		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * map集合转换
	 * 
	 * @param clazz
	 *            类型
	 * @param maps
	 *            map集合
	 * @return
	 */
	public static List<Object> convertMap(Class<?> clazz, List<Map<String, Object>> maps) {
		List<Object> objects = new ArrayList<>();
		for (Map<String, Object> map : maps) {
			objects.add(convertMap(clazz, map));
		}
		return objects;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 */
	public static Map<String, Object> convertBean(Object bean) {
		@SuppressWarnings("rawtypes")
		Class type = bean.getClass();
		Map<String, Object> map = new HashMap<>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					if (result != null) {
						map.put(propertyName, result);
					}
				}
			}
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 转换一个集合
	 * 
	 * @param ts
	 *            集合
	 * @return
	 */
	public static List<Map<String, Object>> convertBean(List<Object> ts) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Object bean : ts) {
			list.add(convertBean(bean));
		}
		return list;
	}
}
