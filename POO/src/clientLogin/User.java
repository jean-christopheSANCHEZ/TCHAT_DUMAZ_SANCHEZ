package clientLogin;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class User implements Serializable{
	
	private String login; //login local de l'utilisateur
	private int uniqueId;//pas utilisé dans notre implementation
	private InetAddress ip;//pas utilise dans notre implementation car tous les test on du etre fait en local donc mm ip, on utilise les numero de port pour simuler differentes machines
	private int numPort; // numéro de port associé à l'utilisateur
	private User [] infoUsers;
	
	
	public User(String login, int uniqueId, InetAddress ip, int numPort) {
		super();
		this.login = login;
		this.uniqueId = uniqueId;
		this.ip = ip;
		this.numPort = numPort;
		
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public int getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public int getNumPort() {
		return numPort;
	}
	public void setNumPort(int numPort) {
		this.numPort = numPort;
	}
	public User[] getInfoUsers() {
		return infoUsers;
	}
	public void setInfoUsers(User[] infoUsers) {
		this.infoUsers = infoUsers;
	}
	
	
	
	
}
