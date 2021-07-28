package org.su18.serialize.test.proxy;

import sun.misc.ProxyGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 生成代理类的几种写法
 *
 * @author su18
 */
public class ProxyGeneratorTest {


	public static void main(String[] args) throws Exception {

		// 设置为 true 后会以文件形式储存生成的代理类
		System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		String className = "PhoneProxy";

		InvocationHandler handler = new SuInvocationHandler(new IPhone());
		byte[]            bytes;
		Phone             phone;

		// 1. 基础写法 Proxy.newProxyInstance
//		phone = (Phone) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Phone.class}, handler);

		// 2.使用 ProxyGenerator 的 generateProxyClass 方法
//		bytes = ProxyGenerator.generateProxyClass(className, new Class[]{Phone.class});

		// 3.使用 ProxyGenerator 的 generateClassFile 方法
		Constructor<?> constructor = ProxyGenerator.class.getDeclaredConstructor(String.class, Class[].class, int.class);
		constructor.setAccessible(true);
		Object proxyGenerator = constructor.newInstance(className, new Class[]{Phone.class}, 49);

		Method m = ProxyGenerator.class.getDeclaredMethod("generateClassFile");
		m.setAccessible(true);
		bytes = (byte[]) m.invoke(proxyGenerator);


		// 使用 Proxy 的 define0 加载类字节码
		Class<?> clazz = ProxyDefineClassTest.defineByProxy(className, bytes);

		// 初始化代理类，加入引用类
		Constructor<?> proxy = clazz.getDeclaredConstructors()[0];
		phone = (Phone) proxy.newInstance(handler);

		phone.call();
	}

}
