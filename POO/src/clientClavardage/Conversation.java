package clientClavardage;

import java.util.*;

import clientLogin.User;


public class Conversation {
	
	private List<Message>  messages = new ArrayList<Message>();//pas utilise car bdd centralisée
	private User user1, user2;//les 2 users prenant place à la conversation
	private int id;//unique id pour bdd
	
	public Conversation( User user1, User user2, int id) {
		super();
		this.user1 = user1;
		this.user2 = user2;
		this.id = id;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	@Override
	public String toString() {
		return "Conversation of " + user1.getLogin() + " to " + user2.getLogin() + ", id=" + id + "";
	}

	public int getId() {
		return id;
	}
	
	
}
