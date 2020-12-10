package clientClavardage;

import java.sql.Timestamp;

public class Message {
	private String data;
	private Timestamp dateEnvoie;
	
	
	public Message(String data, Timestamp dateEnvoie) {
		super();
		this.data = data;
		this.dateEnvoie = dateEnvoie;
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
	
	
	
	
}
