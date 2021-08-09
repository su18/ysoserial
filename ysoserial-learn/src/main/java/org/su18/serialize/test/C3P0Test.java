package org.su18.serialize.test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author su18
 */
public class C3P0Test {

	public static void main(String[] args) throws Exception {

		// 配置文件名为c3p0.properties，会自动加载。
		DataSource dataSource = new ComboPooledDataSource();
		// 获取连接
		Connection conn = dataSource.getConnection();

		String sql = "select * from t66y_user where username = ?";

		// 开启事务设置非自动提交
		conn.setAutoCommit(false);
		// 获得Statement对象
		PreparedStatement statement = conn.prepareStatement(sql);
		// 设置参数
		statement.setString(1, "moodykeke");
		// 执行
		ResultSet resultSet = statement.executeQuery();
		// 提交事务
		conn.commit();

		resultSet.next();

		System.out.println(resultSet.getString("md5"));

		statement.close();
		conn.close();

	}

}
