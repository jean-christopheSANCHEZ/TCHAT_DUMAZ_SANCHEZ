package clientClavardage;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
	        	Message m= new Message("",0);
	            InputStream is = link.getInputStream();
	            int rcv = 0;
	            while ((rcv = is.read()) != 0) {
	            	ObjectInputStream ois = new ObjectInputStream(is);
	            	m=(Message) ois.readObject();
	                System.out.println("Message from "+m.getUser()+" : "+m.getData());
	                
	            }
	            
	            is.close();
	            link.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	
}
