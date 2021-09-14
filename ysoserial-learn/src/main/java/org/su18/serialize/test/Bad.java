package org.su18.serialize.test;

import java.io.IOException;

/**
 * @author su18
 */
public class Bad {

	static {
		try {
			Runtime.getRuntime().exec("open -a Calculator.app");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
