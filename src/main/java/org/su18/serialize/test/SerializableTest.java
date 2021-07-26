package org.su18.serialize.test;

import java.io.*;

/**
 * 序列化类到文件，以及从文件中反序列化类
 *
 * @author su18
 */
public class SerializableTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Person person = new Person("zhangsan", 24);

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("abc.txt"));
		oos.writeObject(person);
		oos.close();


		FileInputStream   fis = new FileInputStream("abc.txt");
		ObjectInputStream ois = new ObjectInputStream(fis);
		ois.readObject();
		ois.close();
	}
}

