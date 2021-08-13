package org.su18.serialize.test.jython;

import org.python.core.*;
import org.python.util.PythonInterpreter;

/**
 * @author su18
 */
public class JythonTest {

	public static void main(String[] args) throws Exception {

		// 获取 resources 路径下的 test.py 文件
		String filePath = JythonTest.class.getClassLoader().getResource("test.py").getPath();

		// 实例化 PythonInterpreter
		PythonInterpreter interpreter = new PythonInterpreter();

		// execfile 方法执行 python 文件
		interpreter.execfile(filePath);
//
//		// exec 方法在本地名称空间中执行Python源字符串
//		interpreter.exec("import os\n" +
//				"os.system(\"open -a Calculator.app\")\n");
//
//		// eval 方法将字符串计算为Python源并返回结果
//		PyObject object = interpreter.eval("1+1");
//		System.out.println(object);
//
//		// 定义函数并调用，传入参数
//		interpreter.exec("import os\ndef add(a,b):\n    return a+b\n");
//
//		PyFunction fun = (PyFunction) interpreter.get("add");
//		PyObject   foo = fun.__call__(new PyObject[]{new PyString("1"), new PyString("2")});
//		System.out.println(foo);

	}

}
