package com.dev.api.json;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.DbUtils;

import com.dev.api.json.beans.Hits;
import com.dev.util.db.ApacheDerby;
import com.dev.util.db.DerbyDbFunctions;
import com.google.gson.Gson;

/**
 * Servlet implementation class DerbyDataPull
 */
@WebServlet("/DerbyDataPull")
public class DerbyDataPull extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public DerbyDataPull() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
	//this is datapull get implementation, based on REQ param it will intercat with DB 
		// pull various data .. return them to caller
		
	// f1 . getting Login user hit count for last two weeks
		//get the connection 
		Connection derbyDefaultConnection=null;
		try{
		 derbyDefaultConnection=ApacheDerby.getDerbyEmbeddedConnection(null);
		/* 
		 ApacheDerby.updateApiViewerHits(derbyDefaultConnection, "ultra-dev", "/home");
			ApacheDerby.updateApiViewerHits(derbyDefaultConnection, "ultra-test", "/test");
			ApacheDerby.updateApiViewerHits(derbyDefaultConnection, "ultra-manager", "/manage");
			ApacheDerby.getHitCount(derbyDefaultConnection);
			*/
		 
		 //not get the HITS bin
		 List<Hits> hits=DerbyDbFunctions.getLastTwoWeekUsage(derbyDefaultConnection);
		// convert to json string
		 
		 Gson gson= new Gson();
		 String hitJson=gson.toJson(hits);
		 System.out.println("hitJson\n"+hitJson);
		 response.getWriter().print(hitJson);
		 response.setHeader("Access-Control-Allow-Origin", "*");
		 response.setContentType("text/json");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			try {
				DbUtils.commitAndClose(derbyDefaultConnection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbUtils.closeQuietly(derbyDefaultConnection);
		}
	/***
	 * sudo logic 
	 * call derby util/function which will return List<Hits> 	
	 */
	
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
