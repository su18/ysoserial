package org.su18.serialize.rmi.server;

import org.su18.serialize.rmi.RemoteInterface;

import java.rmi.Naming;

/**
 * 测试 codebase 时，应把 Server 放在另外的地方执行
 *
 * @author su18
 */
public class RMIServer {

	public static void main(String args[]) throws Exception {

		// 使用自定义 policy 文件
		System.setProperty("java.security.policy", RMIServer.class.getClassLoader().getResource("rmi.policy").toString());
		// 测试 java.rmi.server.codebase
		System.setProperty("java.rmi.server.codebase", "http://127.0.0.1:9999/");

		// 定义 SecurityManager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		RemoteInterface remoteObject = new RemoteObject("Server");

		// sun.rmi.registry.RegistryImpl_Stub
//		Registry registry = LocateRegistry.getRegistry("localhost", 1099);

		// 使用 Naming.rebind 注册 Registry
		// 实际调用 LocateRegistry.getRegistry()

//		registry.bind("remoteObject", remoteObject);
		// 另一种 bind 方法
		Naming.rebind("rmi://localhost:1099/Hello", remoteObject);

		// 在设置 SecurityManager 时，不设这是不允许 bind/rebind/unbind 操作的
//		Naming.rebind("rmi://192.168.31.155:1099/Hello", remoteObject);
	}

}
