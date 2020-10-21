package com.mytekskills.demo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionUtils {

	private static Logger logger = LogManager.getLogger(ConnectionUtils.class);
	
	public static Connection getH2Connection() {
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:h2:~/test", "", "");
		} catch (SQLException e) {
			logger.log(Level.ERROR, ", Error connecting to H2 DB : ", e);
		}
		return conn;
		
	}
	
	public static void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.log(Level.ERROR, ", Error closing connection: ", e);
			}
		}
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.log(Level.ERROR, ", Error closing statement: ", e);
			}
		}
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.log(Level.ERROR, ", Error closing resultset: ", e);
			}
		}
	}
	
}
