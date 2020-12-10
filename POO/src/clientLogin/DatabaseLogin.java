package clientLogin;

import java.sql.*;



public class DatabaseLogin {
	
	private String login;
	private int numPort;
	private String DBurl;
	private Connection con;
	private String requete;
	private Statement stmt;
	
	public DatabaseLogin(String login, int numPort) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			DBurl = "jdbc:mysql://localhost/login?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
			con = DriverManager.getConnection(DBurl, "root", "");
			this.login = login;
			this.numPort = numPort;
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void deconnect() {
		try {
			con.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void insertLoginPort() {
		requete = "INSERT INTO  user (identifiant, port) values ('" + this.login + "', '" + this.numPort+"')";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(requete);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void deleteAllField() {
		requete = "TRUNCATE TABLE user";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(requete);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public int getNumPort() {
		return numPort;
	}


	public void setNumPort(int numPort) {
		this.numPort = numPort;
	}
	
	
	
	
	
	
	
}
