package clientClavardage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

import clientLogin.User;

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
	                new Thread(new TCPmessage(link)).start();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
		}
		
	}
	
	public static class TCPmessage implements Runnable{
		
		final Socket link;
		
		
		public TCPmessage(Socket link) {
			this.link=link;
			
		}
		
		public void run() {
			
	        try {
	        	Message m= new Message("");
	            InputStream is = link.getInputStream();
	            int rcv = 0;
	            
	            	ObjectInputStream ois = new ObjectInputStream(is);
	            	m=(Message) ois.readObject();
	                System.out.println("Message from " + m.getUser().getLogin() + " to : "+ m.getDestinataire().getLogin() +" : "+m.getData());
	                
	                //trouve l'envoyeur du message
	            	// faire une recherche dans BDD conv si conv existe ajout msg sinon ajout conv puis ajout msg
	            	DatabaseConv_mess DB = new DatabaseConv_mess(m.getUser().getLogin(), m.getUser().getNumPort(), m.getDestinataire().getLogin(), m.getDestinataire().getNumPort());
	            	DB.selectConv(m.getUser(), m.getDestinataire());
	            	ResultSet result = DB.getResult();            	
	            	
	            	
	            	try {
	            		if(result.next()) {
	            			System.out.println(result.getString(1));
	            			//ajout du message dans la bdd avec l'id de la conv : getString1)
	            			Message new_message = new Message(m.getData());
	            			DB.insertMessage(new_message, Integer.parseInt(result.getString(1)), m.getUser());
	            		}else {
	            			System.out.println("pas de conv on en creer une");
	            		}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	DB.deconnect();
	            	
	            is.close();
	            link.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	
	public static class TCPstartconv implements Runnable{
		
		final User destination;
		final Message message;
		
		public TCPstartconv(User dest,Message m) {
			this.destination=dest;
			this.message=m;
		}
		
		public void run() {
			try {
				Socket link = new Socket(this.destination.getIp(),this.destination.getNumPort()+1);
				ObjectOutputStream oos=new ObjectOutputStream(link.getOutputStream());
				oos.writeObject(this.message);
				link.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}
}
