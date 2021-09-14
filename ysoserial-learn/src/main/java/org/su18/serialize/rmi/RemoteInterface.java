package org.su18.serialize.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 创建一个远程调用对象的接口，扩展 Remote 接口
 * 所有方法都必须抛出 java.rmi.RemoteException
 *
 * @author su18
 */
public interface RemoteInterface extends Remote {

	public String sayHello() throws RemoteException;

	public String sayGoodbye() throws RemoteException;
}
