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
 *
 * @author su18
 */
public class InvocationHandlerTest {

	public static void main(String[] args) throws Exception {

		HashMap<String, Object> map = new SuMap<>();
		map.put("call", "su18");

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
		phone.call();
	}


	public static class SuMap<K, V> extends HashMap<K, V> {

		@Override
		public V get(Object key) {
			System.out.println("SuMap Get Method Called");
			return super.get(key);
		}
	}

}
