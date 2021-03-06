package clientClavardage;

import java.io.Serializable;
import java.sql.Timestamp;

import clientLogin.User;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;//utilisateur qui envoie le message
	private User destinataire;//utilisateur destinataire
	private String data;//text entr� par l'utilisateur
	private Timestamp dateEnvoie;//format de date pour la date d'envoie
	private int type;//utile pour reconnaitre le type de message dans udp ou tcp
	
	//type = 0 : message de demande de connexion si via le broadcast UDP, ou demande de conversation si via TCP
	//type = 1 : r�ponse d'un broadcast UDP positive
	//type = -1 : r�ponse d'un broadcast UDP n�gative
	//type = 2 : message de deconnexion via broadcast UDP
	
	
	//plusieurs constructeurs selon les besoins des classes utilisant Message
	
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
	
	public Message(User envoyeur, User destinataire, int t) {
		super();
		this.user=envoyeur;
		this.destinataire=destinataire;
		this.type=t;
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
