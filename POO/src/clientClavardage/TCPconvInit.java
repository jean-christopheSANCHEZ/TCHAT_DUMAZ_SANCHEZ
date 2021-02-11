package clientClavardage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.mysql.cj.protocol.Resultset;

import clientClavardage.TCPconvInit.TCPmessage;
import clientLogin.DatabaseLogin;
import clientLogin.User;
import gui.ConversationPage;

public class TCPconvInit {
	
	
	public static class TCPserverconv implements Runnable{
		
		User user;
				
		public TCPserverconv(User utilisateur) {
			this.user=utilisateur;

		}

		
		public void run() {
			try {
	            ServerSocket server = new ServerSocket(user.getNumPort()+1);

	            while(true) {
	                System.out.println("Awaiting connection");
	                Socket link = server.accept();
	                System.out.println("Message recu sur server général");
	                Message m= new Message("");
	                InputStream is;
	    			
	    			try {
	    				is = link.getInputStream();
	    				ObjectInputStream ois = new ObjectInputStream(is);
	    	        	m=(Message) ois.readObject();
	    			} catch (IOException | ClassNotFoundException e1) {
	    				
	    				e1.printStackTrace();
	    			}     
	            	
	            	
	            	DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(),user.getNumPort(),m.getDestinataire().getLogin(),m.getDestinataire().getNumPort());
	            	DB.selectConv(user, m.getUser());
	            	ResultSet result=DB.getResult();
	            	int id;
	            	
	            	try {
	    				if(result.next()) {
	    					 id=Integer.parseInt(result.getString(1));
	    					 Conversation conv=new Conversation(this.user,m.getUser(),id);
	    					 ConversationPage fenetre = new ConversationPage(conv,user,link);
	    					 new Thread(new TCPmessage(link,this.user,fenetre)).start();
	    				}
	    				else {
	    					System.out.println("pas de conv correspondante");
	    				}
	    			} catch (NumberFormatException e1) {
	    				
	    				e1.printStackTrace();
	    			} catch (SQLException e1) {
	    				
	    				e1.printStackTrace();
	    			}
	    			
	    			DB.deconnect();                
	                
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
		}
		
	}
	
	public static class TCPmessage implements Runnable{
		
		final Socket link;
		public ConversationPage fenetre;
		public boolean running = true;
		final User user;
		
		public TCPmessage(Socket link,User u,ConversationPage f) {
			this.link=link;
			this.user=u;
			this.fenetre=f;
			
		}
		
		public boolean sendMessage(Message m) {
			
			try {
				ObjectOutputStream oos=new ObjectOutputStream(this.link.getOutputStream());
				oos.writeObject(m);
				oos.flush();
				return true;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		
		public void run() {
			this.running=true;
			Message m= new Message("");
			
			InputStream is;
			/*
			try {
				is = link.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
	        	m=(Message) ois.readObject();
			} catch (IOException | ClassNotFoundException e1) {
				
				e1.printStackTrace();
			}     
        	
        	
        	DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(),user.getNumPort(),m.getDestinataire().getLogin(),m.getDestinataire().getNumPort());
        	DB.selectConv(user, m.getUser());
        	ResultSet result=DB.getResult();
        	int id;
        	
        	try {
				if(result.next()) {
					 id=Integer.parseInt(result.getString(1));
					 Conversation conv=new Conversation(this.user,m.getUser(),id);
					 this.fenetre=new ConversationPage(conv,user,link);
				}
				else {
					System.out.println("pas de conv correspondante");
				}
			} catch (NumberFormatException e1) {
				
				e1.printStackTrace();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			
			DB.deconnect();
			*/
	        try {
	        	
	        	while(this.running) { 
	        		is = link.getInputStream();
	            
	            
	            	ObjectInputStream ois = new ObjectInputStream(is);
	            	m=(Message) ois.readObject();
	            	
	                System.out.println("Message from " + m.getUser().getLogin() + " to : "+ m.getDestinataire().getLogin() +" : "+m.getData());
	                this.fenetre.mess.append(m.getData());
	                //trouve l'envoyeur du message
	            	// faire une recherche dans BDD conv si conv existe ajout msg sinon ajout conv puis ajout msg
	            	DatabaseConv_mess DB2 = new DatabaseConv_mess(m.getUser().getLogin(), m.getUser().getNumPort(), m.getDestinataire().getLogin(), m.getDestinataire().getNumPort());
	            	DB2.selectConv(m.getUser(), m.getDestinataire());
	            	ResultSet result2 = DB2.getResult();            	
	            	
	            	
	            	try {
	            		if(result2.next()) {
	            			System.out.println(result2.getString(1));
	            			//ajout du message dans la bdd avec l'id de la conv : getString1)
	            			Message new_message = new Message(m.getData());
	            			DB2.insertMessage(new_message, Integer.parseInt(result2.getString(1)), m.getUser());
	            		}else {
	            			System.out.println("pas de conv on en creer une");
	            		}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	String tmp = "                                                                                                                                                " +m.getDateEnvoie() +" : " + m.getData() +"\n";
		            this.fenetre.mess.append(tmp);
	            	DB2.deconnect();
	        	
	            is.close();
	        	}
	            link.close();
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		
	}
	
	public static class TCPstartconv implements Runnable{
		
		final User user;
		final User destination;
		public Conversation conv;
		public Message m;
		
		
		public TCPstartconv(User e,User dest, Conversation c) {
			this.user=e;
			this.destination=dest;
			this.conv=c;
			this.m=new Message(this.user,this.destination,0);
			
		}
		
		public void run() {
			System.out.println("envoie sur le port : "+this.destination.getNumPort());
			try {
				System.out.println("connexion avec le port : "+this.destination.getNumPort());
				Socket link = new Socket(this.destination.getIp(),this.destination.getNumPort()+1);
				ConversationPage fenetre = new ConversationPage(conv,user,link);
				TCPmessage tcp_message= new TCPmessage(link,user,fenetre);
			    Thread thread_message = new Thread(tcp_message);
				thread_message.start();
				ObjectOutputStream oos=new ObjectOutputStream(link.getOutputStream());
				oos.writeObject(m);
				System.out.println("Message envoyé depuis startconv!");
						
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}
}
