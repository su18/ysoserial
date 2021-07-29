package org.su18.serialize.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 自定义的 InvocationHandler
 *
 * @author su18
 */
public class SuInvocationHandler implements InvocationHandler {


	/**
	 * 被代理类（实际类）的引用对象
	 */
	private final Object target;

	/**
	 * 构造方法一般会接收被代理类并创建引用对象，
	 *
	 * @param target 被代理类
	 */
	public SuInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		// 执行动态代理添加的一些逻辑等
		System.out.println("SuInvocationHandler Called");

		return method.invoke(target, args);
	}
}
