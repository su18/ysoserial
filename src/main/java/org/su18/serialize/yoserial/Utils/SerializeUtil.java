package org.su18.serialize.yoserial.Utils;

import java.io.*;

/**
 * @author su18
 */
public class SerializeUtil {


	/**
	 * 将序列化对象写入到文件中
	 *
	 * @param o    对象
	 * @param file 文件名
	 * @throws IOException 抛出 io 异常
	 */
	public static void writeObjectToFile(Object o, String file) throws IOException {
		FileOutputStream   fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(o);
		fos.flush();
		fos.close();
		oos.flush();
		oos.close();
	}

	/**
	 * 从文件中读取序列化对象
	 *
	 * @param file 文件名
	 * @return 返回序列化对象
	 * @throws IOException            抛出异常
	 * @throws ClassNotFoundException 抛出异常
	 */
	public static Object readFileObject(String file) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		return ois.readObject();
	}
}
