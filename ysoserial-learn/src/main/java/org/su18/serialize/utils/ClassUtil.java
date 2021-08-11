package org.su18.serialize.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author su18
 */
public class ClassUtil {

	/**
	 * 使用 Unsafe 来绕过构造方法创建类实例
	 *
	 * @param clazz Class 类型
	 * @return 返回创建的实例
	 * @throws Exception 抛出异常
	 */
	public static Object createInstanceUnsafely(Class<?> clazz) throws Exception {
		// 反射获取Unsafe的theUnsafe成员变量
		Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
		theUnsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe) theUnsafeField.get(null);
		return unsafe.allocateInstance(clazz);
	}

}
