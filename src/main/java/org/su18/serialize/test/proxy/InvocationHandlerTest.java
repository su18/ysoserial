package org.su18.serialize.test.proxy;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * AnnotationInvocationHandler 动态代理测试类
 * 使用 AnnotationInvocationHandler 动态代理，可以触发 memberValues 的 get 方法
 * 如果代理方法有返回值，会使用 invoke 方法的返回值进行强转，不出错的情况下就会返回该返回值
 *
 * @author su18
 */
public class InvocationHandlerTest {

	public static void main(String[] args) throws Exception {

		System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

		HashMap<String, Object> map = new SuMap<>();
		map.put("callback", "adadsdsasdas");

		// 给注解用的动态代理
		String         className    = "sun.reflect.annotation.AnnotationInvocationHandler";
		Class<?>       handlerClass = Class.forName(className);
		Constructor<?> constructor  = handlerClass.getDeclaredConstructors()[0];
		constructor.setAccessible(true);
		InvocationHandler handler = (InvocationHandler) constructor.newInstance(Target.class, map);


		// 有一个成员变量 memberValues 是一个 String/Object 的 Map
		Field memberValues = handlerClass.getDeclaredField("memberValues");
		memberValues.setAccessible(true);
		memberValues.set(handler, map);

		// 调用 AnnotationInvocationHandler 的 invoke 方法
		// 会调用 memberValues 的 get 方法获取 key 为调用方法的 MethodName 的值
		Phone phone = (Phone) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Phone.class}, handler);

		// 调用 phone.callback() 时，会调用 AnnotationInvocationHandler 的 invoke
		// invoke 方法会返回一个 Object 对象
		// phone.callback() 本身也有返回值
		// 这种情况下会尝试强转 AnnotationInvocationHandler 的 invoke 的返回值给 phone.callback() 的返回类型
		System.out.println(phone.callback());
	}

	public static class SuMap<K, V> extends HashMap<K, V> {

		@Override
		public V get(Object key) {
			System.out.println("SuMap Get Method Called");
			return super.get(key);
		}
	}

}
