package org.su18.serialize.test;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author su18
 */
public class StoreableCachingMapTest {

	public static void main(String[] args) throws Exception {

		Class<?>       c           = Class.forName("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap");
		Constructor<?> constructor = c.getDeclaredConstructor(String.class, int.class);
		constructor.setAccessible(true);
		HashMap<String, Object> map = (HashMap) constructor.newInstance("/Users/phoebe/Downloads", 10000);
		map.put("123.txt", "aaa".getBytes(StandardCharsets.UTF_8));
	}

}
