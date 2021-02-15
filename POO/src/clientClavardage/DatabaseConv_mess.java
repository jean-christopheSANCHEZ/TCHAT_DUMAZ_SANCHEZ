package clientClavardage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import clientLogin.User;



//base de donn�e centralisee
public class DatabaseConv_mess {
	private String DBurl;
	private Connection con;
	private String requete;
	private Statement stmt;
	private ResultSet result = null;
	
	private String user, destinataire;
	private int userPort, destinatairePort;
	
	
	//connection a la base de donn�e
	public DatabaseConv_mess(String login, int port, String destLogin, int destPort) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			DBurl = "jdbc:mysql://localhost/conv_mess?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
			con = DriverManager.getConnection(DBurl, "root", "");

			this.user = login;
			this.destinataire = destLogin;
			this.userPort = port;
			this.destinatairePort = destPort;
			
			
			
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
	//selectionner les conv dans la quelle un user apparait
	public void selectConvToMainUser() {
		String requete = "SELECT * FROM conversation WHERE User1 = '" + user +"' or User2 = '" + user +"'";
		try {
			stmt = con.createStatement();
			this.result = stmt.executeQuery(requete);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//selectionner une conv en particulier connaissant les 2 uers
	public void selectConv(User user1, User user2) {
		String requete = "SELECT idConv FROM conversation WHERE User1 = '" + user1.getLogin() +"' AND User2 = '" + user2.getLogin() +"' OR User1 = '" + user2.getLogin() +"' AND User2 = '" + user1.getLogin() +"'";
		try {
			stmt = con.createStatement();
			this.result = stmt.executeQuery(requete);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//ajouter une nouvelle conversation
	public void addConversation() {
		String requete = "INSERT INTO conversation (user1,user2) values ('" + this.user + "','" + this.destinataire + "')";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(requete);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//selectionner la liste des messages suivant leurs id (cle etrangere en lien avec l id de la conv auquel ils sont attch�s
	public void selectListMessageById(int id) {
		String requete = "SELECT * FROM message WHERE id = '" + id +"'";
		try {
			stmt = con.createStatement();
			this.result = stmt.executeQuery(requete);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//ins�rer un nouveau message
	public void insertMessage(Message message, int idConvEnCours, User userEmetteur) {
		String requete = "INSERT INTO message (id,timestamp,data,loginEmetteur) values ('" + idConvEnCours + "','" + message.getDateEnvoie() + "','" + message.getData() +"','" + userEmetteur.getLogin() +"')";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(requete);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public String getDestinataire() {
		return destinataire;
	}



	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}



	public int getUserPort() {
		return userPort;
	}



	public void setUserPort(int userPort) {
		this.userPort = userPort;
	}



	public int getDestinatairePort() {
		return destinatairePort;
	}



	public void setDestinatairePort(int destinatairePort) {
		this.destinatairePort = destinatairePort;
	}


	public ResultSet getResult() {
		return result;
	}
	
	
	
	
	
}
