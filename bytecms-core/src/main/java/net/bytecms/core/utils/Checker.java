package net.bytecms.core.utils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class Checker {

	private Checker() {
		throw new AssertionError("No " + getClass().getName() + " instances for you!");
	}
	
	/**
	 * 断言对象不为null
	 * @param reference 判断的对象
	 * @return 断言正确返回true,否则为false
	 */
	public static <T> boolean BeNotNull(T reference) {
		if (reference == null) {
			return false;
		}
		return true;
	}
	
	public static <T> boolean BeNull(T reference) {
		return !BeNotNull(reference);
	}
	
	/**
	 * 断言集合不为null
	 * @param collection 判断的集合
	 * @return 断言正确返回true,否则为false
	 */
	public static <T> boolean BeNotNull(Iterable<T> collection) {
		if (IterableUtils.isEmpty(collection)) {
			return false;
		}
		return true;
	}
	
	public static <T> boolean BeNull(Iterable<T> collection) {
		return !BeNotNull(collection);
	}
	
	/**
	 * 断言集合不为null且不为空
	 * @param coll 判断的集合
	 * @return 断言正确返回true,否则为false
	 */
	public static <T> Boolean BeNotEmpty(Iterable<T> coll) {
		return !IterableUtils.isEmpty(coll);
	}
	
	public static <T> Boolean BeEmpty(Iterable<T> coll) {
		return !BeNotEmpty(coll);
	}
	
	/**
	 * 断言map不为null且不为空
	 * @param map 判断的map
	 * @return 断言正确返回true,否则为false
	 */
	public static <K, V> boolean BeNotEmpty(Map<K, V> map) {
		return MapUtils.isNotEmpty(map);
	}
	
	public static <K, V> boolean BeEmpty(Map<K, V> map) {
		return !BeNotEmpty(map);
	}
	
	/**
	 * 断言数组不能为空
	 * @param arr 判断的数组
	 * @return 断言正确返回true,否则为false
	 */
	public static <T> Boolean BeNotEmpty(T[] arr) {
		return ArrayUtils.isNotEmpty(arr);
	}
	
	public static <T> Boolean BeEmpty(T[] arr) {
		return !BeNotEmpty(arr);
	}
	
	/**
	 * 断言字符串不为null，不为空
	 * @param cs 判断的字符串
	 * @return 断言正确返回true,否则为false
	 */
	public static Boolean BeNotEmpty(CharSequence cs) {
		return StringUtils.isNotEmpty(cs);
	}
	
	public static Boolean BeEmpty(CharSequence cs) {
		return !BeNotEmpty(cs);
	}
	
	/**
	 * 断言字符串不为null，不为空，不是全空格 
	 * @param cs 判断的字符串
	 * @return 断言正确返回true,否则为false
	 */
	public static Boolean BeNotBlank(CharSequence cs) {
		return StringUtils.isNotBlank(cs);
	}
	
	public static Boolean BeBlank(CharSequence cs) {
		return StringUtils.isBlank(cs);
	}

	public static Boolean BeGreaterThan(Number a, Number b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException("Invalid number parameter, must not be null.");
		}
		return a.doubleValue() - b.doubleValue() > 0;
	}

	public static Boolean BeGreaterOrEqualThan(Number a, Number b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException("Invalid number parameter, must not be null.");
		}
		return a.doubleValue() > b.doubleValue() || a.doubleValue() == b.doubleValue();
	}
	
	public static Boolean BeNotEqual(Object a, Object b) {
		return !a.toString().equals(b.toString());
	}
	
}
