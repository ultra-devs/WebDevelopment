package com.dev.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

public class ApacheDerby {

	private static String DEFAULT_DB_NAME = "DerbyDataBaseEmbedded";
	private static String DEFAULT_TABLE_NAME = "APP_HITS";
	Connection conn1 = null;

	/***
	 * 
	 * getting a deafult derby Connection/creating DB
	 * 
	 * @param dblocation
	 * @return
	 * @throws Exception
	 */
	public static Connection getDerbyEmbeddedConnection(String dblocation) throws Exception {

		Connection derbyConnection = null;

		String defaultenbeddedDBName = "data/" + DEFAULT_DB_NAME;
		String connectDerby = "jdbc:derby:" + defaultenbeddedDBName + ";create=true;";
		// framing/updating the connection String
		if (dblocation != null) {
			connectDerby = "jdbc:derby:" + dblocation + ";create=true;";
		}
		try {
			// loading driver
			String driver = "org.apache.derby.jdbc.EmbeddedDriver"; // ...
			Class.forName(driver).newInstance();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new Exception(e1);
		}
		// driver loading is successful lets createDB/Fetch Connection
		try {
			derbyConnection = DriverManager.getConnection(connectDerby);
			if (derbyConnection != null) {
				// print if
				System.out.println("connected to :"+derbyConnection.getMetaData().getURL());
			}
			// System.out.println(conn1.getMetaData().getURL());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return derbyConnection;

	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {

	}

	// create table
	public static void createAPIVIEWERHITS(Connection connection) {
		String createSql = "CREATE TABLE " + DEFAULT_TABLE_NAME
				+ " (ID INT NOT NULL, LOGINUSER VARCHAR(1000),PAGE VARCHAR(1000),Time_Stamp TimeStamp )";
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(createSql);
			// System.out.println("created APIVIEWER_HITS ");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			DbUtils.closeQuietly(stmt);
		}

	}

	public static void updateApiViewerHits(Connection connection, String login, String page) {

		try {
			createAPIVIEWERHITS(connection);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		String genId = System.currentTimeMillis() + "";
		String id = genId.substring(5, genId.length());

		String stringInsertSql = "Insert into  " + DEFAULT_TABLE_NAME + " values(" + id + "," + "'" + login + "'," + "'"
				+ page + "'," + "CURRENT_TIMESTAMP )";

		// System.out.println(stringInsertSql);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(stringInsertSql);
			// System.out.println(stmt.executeLargeUpdate(stringInsertSql));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			DbUtils.closeQuietly(stmt);
		}

	}

	public static int getHitCount(Connection connection) {
		String fetchHits = "select count(*) from    " + DEFAULT_TABLE_NAME + "";

		Statement stmt = null;
		ResultSet rs = null;
		int basecounter = 58000;
		try {
			stmt = connection.createStatement();
			rs = (stmt.executeQuery(fetchHits));
			if (rs.next()) {
				basecounter = basecounter + rs.getInt(1);
			}

			System.out.println(basecounter);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {

			DbUtils.closeQuietly(stmt);
		}

		return basecounter;
	}

	public static int getLastTwoWeekUsage(Connection connection) {

		String fetchHits = "select count(ID), DATE(Time_Stamp) from    " + DEFAULT_TABLE_NAME
				+ " where Time_Stamp>TIMESTAMP('" + getTwoWeekOldDate() + "', '00.00.00') group by DATE(Time_Stamp)";
		System.out.println(fetchHits);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int basecounter = 58000;
		// Timestamp timestamp = new
		// Timestamp(Calendar.getInstance().getTime().getTime());
		try {
			stmt = connection.prepareStatement(fetchHits);
			// stmt.setTimestamp(1, timestamp);

			rs = stmt.executeQuery();
			if (rs.next()) {
				basecounter = rs.getInt(1);
				System.out.println(basecounter);
			}

			// System.out.println(basecounter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			DbUtils.closeQuietly(stmt);
		}

		return basecounter;
	}

	public static int getLastTwoWeekUserInfo(Connection connection) {

		String fetchHits = "select count(distinct Loginuser), DATE(Time_Stamp) from   " + DEFAULT_TABLE_NAME
				+ " where Time_Stamp>TIMESTAMP('" + getTwoWeekOldDate() + "', '00.00.00') group by DATE(Time_Stamp)";
		System.out.println(fetchHits);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int basecounter = 58000;
		// Timestamp timestamp = new
		// Timestamp(Calendar.getInstance().getTime().getTime());
		try {
			stmt = connection.prepareStatement(fetchHits);
			// stmt.setTimestamp(1, timestamp);

			rs = stmt.executeQuery();
			if (rs.next()) {
				basecounter = rs.getInt(1);
				System.out.println(basecounter);
			}

			// System.out.println(basecounter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			DbUtils.closeQuietly(stmt);
		}

		return basecounter;
	}

	/***
	 * getting usage details
	 *
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *
	 *
	 */
	public List<List<String>> getUsageDeatils() throws InstantiationException, IllegalAccessException {
		List<List<String>> resultCsv = new ArrayList<List<String>>();

		Connection connection=null;
		try {
			connection = getDerbyEmbeddedConnection(null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String getUserusage = " select Loginuser,count(*) hits from " + DEFAULT_TABLE_NAME + "   group by Loginuser";
		// resultCsv.add("SeriealNumber`User`VisitCount");
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.createStatement();
			rs = (stmt.executeQuery(getUserusage));
			StringBuffer line = new StringBuffer();
			List<String> row = new ArrayList<String>();
			int counter = 1;
			while (rs.next()) {
				row = new ArrayList<String>();
				// row.add(counter+"");
				row.add(rs.getString(1));
				row.add(rs.getInt(2) + "");
				// line.append(counter+"`"+rs.getString(1)+"`"+rs.getInt(2)+"`");
				// System.out.println(line);
				counter++;
				resultCsv.add(row);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(connection);

		}

		return resultCsv;

	}

	
	

	
	private static String getTwoWeekOldDate() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();
		String todate = dateFormat.format(date);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -14);
		Date todate1 = cal.getTime();
		System.out.println();
		return dateFormat.format(todate1);

	}

}
