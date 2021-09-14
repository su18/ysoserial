package org.su18.serialize.test.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author su18
 */
public class Change {

	public static void main(String[] args) {
		// 创建ClassPool对象
		ClassPool classPool = ClassPool.getDefault();

		try {
			CtClass ctClass = classPool.get("org.su18.serialize.test.Person");

			// 获取hello方法
			CtMethod helloMethod = ctClass.getDeclaredMethod("hello", new CtClass[]{classPool.get("java.lang.String")});

			// 修改方法的访问权限为private
			helloMethod.setModifiers(Modifier.PRIVATE);

			// 输出hello方法的content参数值
			helloMethod.insertBefore("System.out.println($1);");

			// 输出hello方法的返回值
			helloMethod.insertAfter("System.out.println($_); return \"Return:\" + $_;");

			File classFilePath = new File(new File(System.getProperty("user.dir"), "ysoserial-su18/ysoserial-learn/src/main/java/org/su18/serialize/test/"), "Person.class");

			// 使用类CtClass，生成类二进制
			byte[] bytes = ctClass.toBytecode();

			// 将class二进制内容写入到类文件
			FileUtils.writeByteArrayToFile(classFilePath, bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
