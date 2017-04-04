package com.dev.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.dev.api.json.beans.Hits;
import com.dev.util.common.DateUtility;

public class DerbyDbFunctions {
	
	private static String DEFAULT_TABLE_NAME = "APP_HITS";
	public static List<Hits> getLastTwoWeekUsage(Connection connection) {

		List<Hits> resutList= new ArrayList<>();
		String[] colors={"#ee4339","#ee9336", "#eed236","#d3ee36","#a7ee70","#58dccd","#36abee","#476cee","#a244ea","#e33fc7"};
		String fetchHits = "select count(ID), DATE(Time_Stamp) from    " + DEFAULT_TABLE_NAME
				+ " where Time_Stamp>TIMESTAMP('" + DateUtility.getTwoWeekOldDate(null) + "', '00.00.00') group by DATE(Time_Stamp)";
		System.out.println(fetchHits);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(fetchHits);
			Hits hit=null;
			// stmt.setTimestamp(1, timestamp);

			rs = stmt.executeQuery();
			int counter=1;
			int selectindex=0;
			while (rs.next()) {
				hit= new Hits();
				hit.setCount(rs.getString(1));
				hit.setDate(DateUtility.convertDataFormat("yyyy-MM-dd","MMM-dd",rs.getString(2)));
				hit.setId(counter++);
				hit.setColor(colors[selectindex++]);
				if(selectindex==colors.length)
				{
					//reset
					selectindex=0;
				}
				resutList.add(hit);
			    
			}

			// System.out.println(basecounter);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			DbUtils.closeQuietly(stmt);
		}

		return resutList;
	}
	
	public static int getLastTwoWeekUserInfo(Connection connection) {

		String fetchHits = "select count(distinct Loginuser), DATE(Time_Stamp) from   " + DEFAULT_TABLE_NAME
				+ " where Time_Stamp>TIMESTAMP('" +  DateUtility.getTwoWeekOldDate(null)  + "', '00.00.00') group by DATE(Time_Stamp)";
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

}
