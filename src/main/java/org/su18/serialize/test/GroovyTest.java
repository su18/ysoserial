package org.su18.serialize.test;

import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.runtime.MethodClosure;

import java.lang.reflect.Method;
import java.util.HashSet;

/**
 * @author su18
 */
public class GroovyTest {


	public static Closure<?> buildClosure(String string) {
		return (Closure<?>) new GroovyShell().evaluate(string);
	}


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




		MethodClosure mc = new MethodClosure(GroovyTest.buildClosure("\"open -a Calculator.app\""), "execute");
		Method        m  = MethodClosure.class.getDeclaredMethod("doCall", Object.class);
		m.setAccessible(true);
		m.invoke(mc, new HashSet<>());

	}
}
