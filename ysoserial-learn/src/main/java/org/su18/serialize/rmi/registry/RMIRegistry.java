package org.su18.serialize.rmi.registry;

import java.rmi.registry.LocateRegistry;

/**
 * Registry 注册表就是一个 RMI 电话本
 * 可以通过 Registry 来查找另一台主机上已注册远程对象的引用
 * 注册表通过 Naming 来实现
 *
 * @author su18
 */
public class RMIRegistry {

	public static void main(String args[]) {
		try {

			// 创建 Registry，返回 sun.rmi.registry.RegistryImpl
			// 其内部的 ref 也就是 sun.rmi.server.UnicastServerRef
			// 持有sun.rmi.registry.RegistryImpl_Skel 类型的对象变量ref
			LocateRegistry.createRegistry(1099);

			System.out.println("Server Start");
			
			Thread.sleep(1000000000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
