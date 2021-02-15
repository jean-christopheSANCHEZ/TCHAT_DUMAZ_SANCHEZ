package clientLogin;

import java.sql.*;

//base de donnee locale

public class DatabaseLogin {
	
	private String login;
	private int numPort;
	private String DBurl;
	private Connection con;
	private String requete;
	private Statement stmt;
	private ResultSet result = null;
	
	//connection a la bdd
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
	//deconnexion de la bdd
	public void deconnect() {
		try {
			con.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//d'un utilisateur (couple login port)
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
	
	
	//vider la table
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
	//selectionne une ligne en fonction du login pour avoir le num de port par exemple
	public void selectUserByLogin(String login) {
		String requete = "SELECT * FROM user WHERE identifiant = '" + login +"'";
		try {
			stmt = con.createStatement();
			this.result = stmt.executeQuery(requete);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//selectionne tous les utilisateurs presents dans la bdd
	public void selectUsers() {
		String requete = "SELECT * FROM user";
		try {
			stmt = con.createStatement();
			this.result = stmt.executeQuery(requete);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//supprime un utilisateur en particulier
	public void deleteByLogin(String login) {
		requete = "DELETE FROM user WHERE login='"+login +"'";
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

	public ResultSet getResult() {
		return result;
	}

	
	
}
