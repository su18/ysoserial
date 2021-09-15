package org.su18.serialize.rmi.client;

import java.io.Serializable;

/**
 * @author su18
 */
public class Su18 implements Serializable {

	@Override
	public String toString() {
		System.out.println("Where am I?");
		return "su18";
	}
}
