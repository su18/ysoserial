package org.su18.serialize.test.jython;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author su18
 */
public class ScriptEngineTest {

	public static void main(String[] args) throws ScriptException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");
		engine.eval("import sys");
		engine.eval("print sys");
		engine.put("a", 42);
		engine.eval("print a");
		engine.eval("x = 2 + 2");
		Object x = engine.get("x");
		System.out.println("x: " + x);
	}

}
