package org.su18.serialize.test;


import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author su18
 */
public class RhinoTest {


	public static void scriptEngineTest() throws Exception {

		// 获得脚本引擎
		ScriptEngineManager sem    = new ScriptEngineManager();
		ScriptEngine        engine = sem.getEngineByName("javascript");

		// 定义函数
		// 实际是调用 RhinoScriptEngine 的 eval() 方法
		engine.eval("function add (a, b) {var sum = a + b; return sum; }");

		// 取得调用接口
		Invocable jsInvoke = (Invocable) engine;

		//执行脚本中定义的方法
		Object result1 = jsInvoke.invokeFunction("add", 13, 20);
		System.out.println(result1);

	}


	public static void main(String[] args) throws Exception {

//		scriptEngineTest();

		// 初始化 Context 及标准对象
		Context    ctx   = Context.enter();
		Scriptable scope = ctx.initStandardObjects();

		// 调用 JS 脚本
		String jsStr  = "function add (a, b) {var sum = a + b; return sum; } add(10,20)";
		Object result = ctx.evaluateString(scope, jsStr, null, 0, null);
		System.out.println("result=" + result);
	}

}
