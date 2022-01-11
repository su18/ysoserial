package org.su18.serialize.ignite;

import org.apache.ignite.internal.util.GridJavaProcess;
import org.apache.ignite.internal.util.ipc.IpcEndpoint;
import org.apache.ignite.internal.util.ipc.IpcServerEndpoint;
import org.apache.ignite.internal.util.ipc.shmem.IpcSharedMemoryServerEndpoint;
import org.apache.ignite.internal.util.typedef.X;
import org.apache.ignite.internal.util.typedef.internal.U;

import java.io.InputStream;

/**
 *
 * https://github.com/apache/ignite/blob/master/modules/core/src/main/java/org/apache/ignite/internal/util/ipc/shmem/IpcSharedMemoryServerEndpoint.java
 *
 * @author su18
 */
public class IgniteTest {

	public static void main(String[] args) {

		System.out.println("Starting server ...");

		// Tell our process PID to the wrapper.
		X.println(GridJavaProcess.PID_MSG_PREFIX + U.jvmPid());

		InputStream is = null;

		try {
			IpcServerEndpoint srv = new IpcSharedMemoryServerEndpoint(U.defaultWorkDirectory());

			new IgniteTestResources().inject(srv);

			srv.start();

			System.out.println("IPC shared memory server endpoint started");

			IpcEndpoint clientEndpoint = srv.accept();

			is = clientEndpoint.inputStream();

			for (; ; ) {
				X.println("Before read.");

				is.read();

				Thread.sleep(50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			U.closeQuiet(is);
		}
	}

}
