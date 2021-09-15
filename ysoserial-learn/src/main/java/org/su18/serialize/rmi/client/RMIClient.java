package org.su18.serialize.rmi.client;

import org.su18.serialize.rmi.RemoteInterface;
import org.su18.serialize.rmi.server.RemoteObject;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * @author su18
 */
public class RMIClient {

	public static void main(String[] args) {
		try {
			// sun.rmi.registry.RegistryImpl_Stub
			Registry registry = LocateRegistry.getRegistry("localhost", 1099);

			System.out.println(Arrays.toString(registry.list()));

			// lookup and call
			RemoteInterface stub = (RemoteInterface) registry.lookup("Hello");
			System.out.println(stub.sayHello());
			System.out.println(stub.sayGoodbye());

			// rebind
			registry.rebind("Hello", new RemoteObject("client"));

			// lookup and call
			RemoteInterface stub2 = (RemoteInterface) registry.lookup("Hello");
			System.out.println(stub2.sayHello());
			System.out.println(stub2.sayGoodbye());

			// Naming.lookup 重新
//			RemoteInterface remoteObject = (RemoteInterface) Naming.lookup("rmi://localhost:1099/remoteObject");
//			System.out.println(remoteObject.sayHello());
//			System.out.println(remoteObject.sayGoodbye());
			Thread.sleep(100000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
