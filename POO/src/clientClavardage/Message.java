package clientClavardage;

import java.io.Serializable;
import java.sql.Timestamp;

import clientLogin.User;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private User destinataire;
	private String data;
	private Timestamp dateEnvoie;
	private int type;
	
	public Message(String data) {
		super();
		this.data = data;
		this.dateEnvoie = new Timestamp(System.currentTimeMillis());
	}
	
	public Message(String data, User envoyeur, User destinataire) {
		super();
		this.data = data;
		this.user = envoyeur;
		this.destinataire = destinataire;
		this.dateEnvoie = new Timestamp(System.currentTimeMillis());
	}
	
	public Message(String data, int type) {
		super();
		
		this.data = data;
		this.dateEnvoie = new Timestamp(System.currentTimeMillis());
		this.type = type;
		
		
		
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Timestamp getDateEnvoie() {
		return dateEnvoie;
	}
	public void setDateEnvoie(Timestamp dateEnvoie) {
		this.dateEnvoie = dateEnvoie;
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public User getDestinataire() {
		return this.destinataire;
	}

	public void setDestinataire(User user) {
		this.destinataire = user;
	}
	
	
	
}
