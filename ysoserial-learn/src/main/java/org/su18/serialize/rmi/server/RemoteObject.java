package org.su18.serialize.rmi.server;

import org.su18.serialize.rmi.RemoteInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 远程调用方法实现类
 * 通常会扩展 java.rmi.UnicastRemoteObject，这是一个与 Object 等效的类
 * UnicastRemoteObject 为子类提供了 exportObject 方法，以及 equals hashcode toString
 * 以及网络通信等方法
 * 如果不继承 UnicastRemoteObject 则需要调用 exportObject 方法，为目标类创建动态代理
 * sun.rmi.server.Util.createProxy --> RemoteObjectInvocationHandler
 * <p>
 * Java本身对 RMI 规范的实现默认使用的是 JRMP 协议，而 Weblogic 对 RMI 规范的实现使用 T3 协议
 *
 * @author su18
 */
public class RemoteObject extends UnicastRemoteObject implements RemoteInterface {

	private String name;

	/**
	 * 必须显示声明构造函数，并抛出异常
	 *
	 * @throws RemoteException 异常
	 */
	public RemoteObject(String name) throws RemoteException {
		this.name = name;
	}

	@Override
	public String sayHello() throws RemoteException {
		return "This is RemoteObject!" + name;
	}

	@Override
	public String sayHello(Object someone) throws RemoteException {
		return "This is RemoteObject!" + someone.toString();
	}

	@Override
	public String sayGoodbye() throws RemoteException {
		return "Bye~" + name;
	}


}
