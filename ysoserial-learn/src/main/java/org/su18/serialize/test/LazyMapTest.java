package org.su18.serialize.test;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author su18
 */
public class LazyMapTest {

	public static HashMap<Integer, String> hashMap = new HashMap<>();

	/**
	 * 创建自定义 keyTransformer 在原有值上+1
	 */
	public static Transformer keyTransformer = new Transformer() {
		@Override
		public Object transform(Object input) {
			int num = (int) input;
			num += 1;
			return num;
		}
	};

	public static void main(String[] args) {

		// 自定义 Transformer
		hashMap.put(1, "a");

		// 使用 LazyMap
		Map map = LazyMap.decorate(hashMap, keyTransformer);

		// get() 不存在时调用 Transformer 的 transform 方法处理并返回
		System.out.println(map.get(1010101010));
	}

}
