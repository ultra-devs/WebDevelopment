package com.dev.util.test;

import java.sql.Connection;
import java.sql.SQLException;

import com.dev.util.db.ApacheDerby;

public class TestExe {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Connection connection=null;
		try {
			connection = ApacheDerby.getDerbyEmbeddedConnection(null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (connection != null) {
			try {
				// System.out.println(connection.getMetaData().getURL());
				// createAPIVIEWERHITS(connection);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					connection.commit();
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

}
