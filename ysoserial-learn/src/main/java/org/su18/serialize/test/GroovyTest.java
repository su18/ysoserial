package org.su18.serialize.test;

import org.codehaus.groovy.runtime.MethodClosure;

import java.lang.reflect.Method;

/**
 * @author su18
 */
public class GroovyTest {


	public static void main(String[] args) throws Exception {
//		GroovyClassLoader classLoader = new GroovyClassLoader();
//		Class groovyClass = classLoader.parseClass("def cal(int a, int b){\n" +
//				"    return a+b\n" +
//				"}");
//		try {
//			Object[]     param        = {8, 7};
//			GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
//			int          result       = (int) groovyObject.invokeMethod("cal", param);
//			System.out.println(result);
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}

		// 第一种写法，用 JAVA 对象的 Runtime.getRuntime() 执行 exec 方法
		MethodClosure mc = new MethodClosure(Runtime.getRuntime(), "exec");
		Method        m  = MethodClosure.class.getDeclaredMethod("doCall", Object.class);
		m.setAccessible(true);
		m.invoke(mc, "open -a Calculator.app");

		// 第二种写法，用 Groovy 的 String 对象的 execute 方法执行命令
		MethodClosure methodClosure = new MethodClosure("open -a Calculator.app", "execute");
		methodClosure.call();

	}
}
