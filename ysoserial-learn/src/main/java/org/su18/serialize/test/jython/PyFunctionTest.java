package org.su18.serialize.test.jython;

import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

/**
 * @author su18
 */
public class PyFunctionTest {

	public static void main(String[] args) {

		// 实例化 PythonInterpreter
		PythonInterpreter interpreter = new PythonInterpreter();

		// 定义函数并调用，传入参数
		interpreter.exec("import os\n" +
				"\n" +
				"def open():\n" +
				"    os.system(\"open -a Calculator.app\")\n");

		PyFunction fun = (PyFunction) interpreter.get("open");
		PyObject   foo = fun.__call__();
		System.out.println(foo);


	}

}
