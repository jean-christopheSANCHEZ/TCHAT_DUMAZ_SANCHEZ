package clientClavardage;

import java.sql.Timestamp;

public class Message {
	private String data;
	private Timestamp dateEnvoie;
	private int type;
	
	public Message(String data) {
		super();
		this.data = data;
		this.dateEnvoie = dateEnvoie;
	}
	
	public Message(String data, int type) {
		super();
		
		this.data = data;
		this.dateEnvoie = dateEnvoie;
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
	
	
	
	
}
