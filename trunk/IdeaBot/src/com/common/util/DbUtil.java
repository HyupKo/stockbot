package com.common.util;

import java.sql.*;

public class DbUtil {
	private Connection conn;
	private Statement stmt;
	private ResultSet rs = null;
	public DbUtil(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	private void connectDb(){
		try{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ibot", "ibot", "rhksfl!180~");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private void closeDb(){
		try{
			if(this.conn!=null)conn.close();
		}
		catch(Exception e){}

	}
	public ResultSet queryResult(String query){
		this.connectDb();
		try{
			this.stmt = this.conn.createStatement();
			this.rs = this.stmt.executeQuery(query);
		}
		catch(SQLException e){
			this.closeDb();
			e.printStackTrace();
		}
		return this.rs;
	}
	
}
