package org.su18.serialize.rmi.client;

import org.su18.serialize.rmi.RemoteInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author su18
 */
public class Client {

	public static void main(String[] args) {
		try {

			// sun.rmi.registry.RegistryImpl_Stub
			Registry        registry = LocateRegistry.getRegistry("localhost", 1099);
			RemoteInterface stub     = (RemoteInterface) registry.lookup("remoteObject");
			System.out.println(stub.sayHello());
			System.out.println(stub.sayGoodbye());

			// 指定 SecurityManager 增加安全性
//			System.setSecurityManager(new RMISecurityManager());
//			System.setSecurityManager(new SecurityManager());

			// lookup
//			RemoteInterface remoteObject = (RemoteInterface) Naming.lookup("rmi://localhost:1099/remoteObject");
//			System.out.println(remoteObject.sayHello());
//			System.out.println(remoteObject.sayGoodbye());
			Thread.sleep(100000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
