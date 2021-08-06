package org.su18.serialize.test;

import bsh.Interpreter;
import bsh.XThis;

/**
 * @author su18
 */
public class BeanShellTest {


	public static void main(String[] args) throws Exception {

		Interpreter interpreter = new Interpreter();

//		String payload = "exec(poc)";

		String func = "exec(Object cmd) {java.lang.Runtime.getRuntime().exec(cmd);}";
		interpreter.eval(func);

//		interpreter.set("poc", "open -a Calculator.app");

//		interpreter.eval(payload);

		XThis xThis = new XThis(interpreter.getNameSpace(), interpreter);
		xThis.invokeMethod("exec", new Object[]{"open -a Calculator.app"});
	}

}
