package org.su18.serialize.test;

import clojure.core$constantly$fn__4614;
import clojure.lang.Compiler;
import clojure.lang.LineNumberingPushbackReader;
import clojure.lang.LispReader;
import clojure.lang.RT;
import clojure.main$eval_opt;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author su18
 */
public class ClojureTest {

	public static void main(String[] args) throws Exception {

		RT.var("clojure.core", "*read-eval*");

		String payload = "(use '[clojure.java.shell :only [sh]])(sh\"open\" \"-a\" \"Calculator.app\")";

		// 使用 load 解析 clojure 代码
		Reader reader = new InputStreamReader(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)), RT.UTF8);
//		Compiler.load(reader);

		// 使用 loadFile 也可
//		Compiler.loadFile("/Users/phoebe/IdeaProjects/ysoserial-su18/ysoserial-learn/src/main/java/org/su18/serialize/ysoserial/Clojure/main.clj");

		// 本质都是使用 clojure.lang.Compiler.eval(java.lang.Object, boolean) 方法
		LineNumberingPushbackReader pushbackReader = new LineNumberingPushbackReader(reader);

		// 循环读取并调用 Compiler.eval 方法
		for (Object r = LispReader.read(pushbackReader, null); !Objects.equals(r, new Object()); r = LispReader.read(pushbackReader, null)) {
			Compiler.eval(r, false);
		}

		// 调用此函数可以直接解析
		main$eval_opt.invokeStatic(payload);

		core$constantly$fn__4614 c = new core$constantly$fn__4614("aaa");
		Object                   a = c.invoke("bbb");
		System.out.println(a);
	}

}
