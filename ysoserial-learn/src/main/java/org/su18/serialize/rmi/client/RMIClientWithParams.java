package org.su18.serialize.rmi.client;

import org.su18.serialize.rmi.RemoteInterface;

import java.rmi.Naming;

/**
 * 如果 Client 端调用服务端方法时传入的类是服务端不存在的，则会抛出 ClassNotFoundException
 * 此时如果服务端设置了 java.rmi.server.useCodebaseOnly=false
 * 除了本地 classpath，服务端还会尝试从 java.rmi.server.codebase 配置的远程地址获取 .class 文件
 * 例如 类名 org.su18.serialize.rmi.client.Su18 codebase http://127.0.0.1:9999
 * 调用时 Server 就会尝试获取 http://127.0.0.1:9999/org/su18/serialize/rmi/client/Su18.class 的类并加载
 *
 * @author su18
 */
public class RMIClientWithParams {

	public static void main(String[] args) throws Exception {

		RemoteInterface stub = (RemoteInterface) Naming.lookup("rmi://localhost:1099/Hello");

		// 如果 Client 给 Server 传入了 Client 的类，Server 也会尝试加载
		// 因为最终调用是在 Server 端进行的
		// 这个作为参数的类也必须可以反序列化
		System.out.println(stub.sayHello(new Su18()));
		System.out.println(stub.sayGoodbye());
	}

}
