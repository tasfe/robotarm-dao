package robot.arm.dao.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * xxx转bean工具类
 * 
 * 说明：v0.3 增加转bean时驼锋命名和下划线命名自动映射
 * 
 * v0.2 增加json转bean; json转bean list
 * 
 * v0.1 提供将map转bean list
 * 
 * @author li.li
 * 
 */
public class BeanUtils {
	private static final Logger log = Logger.getLogger(BeanUtils.class);

	private static final Class<?>[] EMPTY_CLASSES = new Class<?>[0];
	private static Pattern p = Pattern.compile("[A-Z]+");// 字母大写
	private static String UNDERLINE = "_";// 下划线

	/**
	 * map转成beans
	 * 
	 * @param <F>
	 * @param <T>
	 * @param col
	 * @param clazz
	 * @return
	 */
	public static <F, T> List<T> toBeans(Collection<F> col, final Class<T> clazz) {
		if (col == null || col.isEmpty() || clazz == null) {
			return Collections.emptyList();// 返回空
		}
		Collection<T> c2 = Collections2.transform(col, new Function<F, T>() {

			@Override
			@SuppressWarnings("unchecked")
			public T apply(F from) {
				try {
					Field[] fields = clazz.getDeclaredFields();
					Object bean = clazz.newInstance();

					Map<Object, Object> dbMap = null;
					if (from instanceof Map<?, ?>)
						dbMap = (Map<Object, Object>) from;
					else
						return null;// 返回空

					for (Field f : fields) {
						String fieldName = f.getName();
						Object fieldValue = dbMap.get(humpToUnderline(fieldName));// 驼锋命名转为下划线命名
						Method writeMethod = new PropertyDescriptor(fieldName, clazz).getWriteMethod();
						writeMethod.invoke(bean, fieldValue);
					}

					return clazz.cast(bean);
				} catch (Throwable e) {
					if (e instanceof InstantiationException)
						log.error("Bean需要一个无参的构造函数");

					log.error(e.getMessage(), e);
				}

				return null;// 返回空
			}

		});

		return new ArrayList<T>(c2);
	}

	public static <T> Map<String, Object> toMap(T bean) {
		Field[] fields = bean.getClass().getDeclaredFields();

		Map<String, Object> map = new HashMap<String, Object>(fields.length);

		try {
			for (Field field : fields) {
				Method readMethod = new PropertyDescriptor(field.getName(), bean.getClass()).getReadMethod();
				Object value = readMethod.invoke(bean, new Object[0]);
				map.put(humpToUnderline(field.getName()), value);
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}

		return map;
	}

	/**
	 * json转成beans
	 * 
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return bean colleciton
	 */
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> toBeans(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json) || clazz == null) {
			return Collections.emptyList();
		}

		JSONArray ja = JSONArray.fromObject(json);
		Collection<T> beans = JSONArray.toCollection(ja, clazz);

		return beans;
	}

	/**
	 * json转成bean
	 * 
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return bean
	 */
	public static <T> T toBean(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json) || clazz == null) {
			return null;
		}

		JSONObject jo = JSONObject.fromObject(json);
		Object obj = JSONObject.toBean(jo, clazz);

		return clazz.cast(obj);
	}

	/**
	 * 下划线命名转驼峰
	 */
	public static String underlineToHump(String name) {
		String[] array = StringUtils.split(name, UNDERLINE);
		StringBuilder sb = new StringBuilder((array.length - 1) * 2 + 1);

		for (int i = 0; i < array.length; i++) {

			if (i == 0)
				sb.append(array[i]);
			else {
				sb.append(StringUtils.substring(array[i], 0, 1).toUpperCase());
				sb.append(StringUtils.substring(array[i], 1));
			}
		}

		return sb.toString();
	}

	/**
	 * 驼峰转下划线命名
	 * 
	 */
	public static String humpToUnderline(String name) {
		StringBuilder sb = new StringBuilder();

		for (char c : name.toCharArray()) {
			String cs = String.valueOf(c);

			if (p.matcher(cs).find()) {
				sb.append(UNDERLINE);
				sb.append(StringUtils.lowerCase(cs));
			} else
				sb.append(cs);
		}

		return sb.toString();
	}

	/**
	 * 类名转下划线命名
	 * 
	 */
	public static String clazzToUnderline(String name) {
		StringBuilder sb = new StringBuilder();

		char[] cs = name.toCharArray();

		for (int i = 0; i < cs.length; i++) {
			String c = String.valueOf(cs[i]);

			if (p.matcher(c).find()) {
				if (i != 0)
					sb.append(UNDERLINE);
				sb.append(StringUtils.lowerCase(c));
			} else
				sb.append(c);
		}

		return sb.toString();
	}
	
	  public static Class<?>[] getActualClass(Type genericType) {

	        if (genericType instanceof ParameterizedType) {

	            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
	            Class<?>[] actualClasses = new Class<?>[actualTypes.length];

	            for (int i = 0; i < actualTypes.length; i++) {
	                Type actualType = actualTypes[i];
	                if (actualType instanceof Class<?>) {
	                    actualClasses[i] = (Class<?>) actualType;
	                } else if (actualType instanceof GenericArrayType) {
	                    Type componentType = ((GenericArrayType) actualType).getGenericComponentType();
	                    actualClasses[i] = Array.newInstance((Class<?>) componentType, 0).getClass();
	                }
	            }

	            return actualClasses;
	        }

	        return EMPTY_CLASSES;
	    }

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////

	public static class TestBean {
		private int id;
		private String name;
		private String desc;
		private long test1;
		private boolean test2;
		private List<String> test3;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public List<String> getTest3() {
			return test3;
		}

		public void setTest3(List<String> test3) {
			this.test3 = test3;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getTest1() {
			return test1;
		}

		public void setTest1(long test1) {
			this.test1 = test1;
		}

		public boolean isTest2() {
			return test2;
		}

		public void setTest2(boolean test2) {
			this.test2 = test2;
		}

		@Override
		public String toString() {
			return id + "|" + name + "|" + desc + "|" + test1 + "|" + test2 + "|" + test3;
		}
	}
	
	

	// 测试
	public static void main(String[] args) {
		// //////////测试map//////////
		// 从db读取出来的数据
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();

		Map<Object, Object> map = new HashMap<Object, Object>(5);
		map.put("id", 1234);
		map.put("name", "我是中国人");
		map.put("desc", "我在中国北京");
		map.put("test1", 899l);
		map.put("test2", true);
		map.put("test3", Arrays.asList("123", "456"));
		list.add(map);
		map = new HashMap<Object, Object>(5);
		map.put("id", 12345);
		map.put("name", "我是中国人2");
		map.put("desc", "我在中国北京2");
		map.put("test1", 8992l);
		map.put("test2", false);
		map.put("test3", Arrays.asList("abcdef", "def"));
		list.add(map);

		long t = System.currentTimeMillis();
		Collection<TestBean> col = BeanUtils.toBeans(list, TestBean.class);// map转beans
		System.out.println("执行时间：" + (System.currentTimeMillis() - t));
		System.out.println("数量：" + col.size());
		System.out.println(JSONArray.fromObject(list));
		System.out.println("####" + col.iterator().next().getTest3());

		// //////////测试json//////////
		String json = "[{\"id\":1234,\"test1\":899,\"desc\":\"我在中国北京\",\"test3\":[\"abcdef\",\"def\"],\"test2\":true,\"name\":\"我是中国人\"},{\"id\":12345,\"test1\":8992,\"desc\":\"我在中国北京2\",\"test2\":false,\"name\":\"我是中国人2\",\"test3\":[\"12345\",\"88776666\"]}]";
		String json2 = "{\"id\":1234,\"test1\":899,\"desc\":\"老家在东北\",\"test2\":true,\"name\":\"我是中国人\"}";
		Collection<TestBean> beans = BeanUtils.toBeans(json, TestBean.class);// json转beans
		TestBean bean = BeanUtils.toBean(json2, TestBean.class);// json转bean
		System.out.println("@@@@@@@@@@@json转beans:  " + beans.iterator().next().getTest3());
		System.out.println("@@@@@@@@@@@json转bean:  " + bean.getDesc());
	}

	// public static void main(String[] args) throws InstantiationException,
	// IllegalAccessException {
	// Object bean = TestBean.class.newInstance();
	// System.out.println(bean);
	// }

}
