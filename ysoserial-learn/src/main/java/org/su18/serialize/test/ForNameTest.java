package org.su18.serialize.test;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author su18
 */
public class ForNameTest {


	public static void main(String[] args) throws Exception {

		URLClassLoader loader = new URLClassLoader(new URL[]{new URL("file:///Users/phoebe/Downloads/1.jar")});
		Class<?>       c      = Class.forName("org.su18.serializable.PureEvilClass", true, loader);
		c.newInstance();
	}

}
