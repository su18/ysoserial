package org.su18.serialize.test;

import java.io.IOException;
import java.io.Serializable;

/**
 * 序列化测试类
 *
 * @author su18
 */
public class Person implements Serializable {

	static {

		System.out.println("static call");
	}

	private String name;

	private int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		Runtime.getRuntime().exec("open -a Calculator.app");
	}

	public static String hello(String some) {
		return some;
	}

}