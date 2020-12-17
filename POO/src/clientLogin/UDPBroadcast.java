package clientLogin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import clientClavardage.Message;

public class UDPBroadcast {
	
	
	public static class UDPserver implements Runnable{
		String name="";
		User user;	
		
		public UDPserver(String Name, User utilisateur) {
			this.name=Name;
			this.user=utilisateur;
			
		}
		
		public void run() {
			
			try {
					
			System.out.println("Thread ServeurUDP started");
			DatagramSocket serveur = new DatagramSocket(2000);
			
			byte[] buffer = new byte[1024];
			Message m=new Message("");
			
			DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
			
			while (true) {
			
			serveur.receive(inPacket);
         	   
         	ByteArrayInputStream ArrayStream2 = new ByteArrayInputStream(inPacket.getData());
            ObjectInputStream ObjectStream2 = new ObjectInputStream(ArrayStream2);
            try {
            	m = (Message) ObjectStream2.readObject();
            } catch (ClassNotFoundException e) {
					System.exit(-1);
					e.printStackTrace();
            }
			
			InetAddress clientAddress = inPacket.getAddress();
			int clientPort = inPacket.getPort();
			
			
			if(m.getType()==0) {
				if(m.getData()!=this.user.getLogin()) {
				Message m2=new Message(this.user.getLogin(),1);
				}
				else if(m.getData()==this.user.getLogin()){
					Message m2=new Message(this.user.getLogin(),-1);
				}
				ByteArrayOutputStream ArrayStream = new ByteArrayOutputStream();
	            try {
	            	ObjectOutputStream ObjectStream = new ObjectOutputStream(ArrayStream);
	                ObjectStream.writeObject(m);
	            } catch(IOException e) {
	                System.err.println("Erreur lors de la s�rialisation : " + e);
	                System.exit(-1);
	            }
	              
	            byte[] buffer2 = ArrayStream.toByteArray();
	            DatagramPacket outPacket = new DatagramPacket(buffer2, buffer2.length, clientAddress, clientPort);
	   			serveur.send(outPacket);
	   			// + update base de donn�es local
			}
	   		else if(m.getType()==2) {
				// retirer de la base de donn�es local
			}
			
			
			}
			
				}catch (SocketException e) {
		               e.printStackTrace();
		        } catch (UnknownHostException e) {
		               e.printStackTrace();
		        } catch (IOException e) {
		               e.printStackTrace();
		        }
			
		}
	}

	   
	public static class UDPclientBroadcast implements Runnable{
	      String login = "";	      
	      User user;
	      
	      public UDPclientBroadcast(String pName, User utilisateur){
	         this.login = utilisateur.getLogin();
	         this.user=utilisateur;
	      }
	      
	      public void run(){
	    	 System.out.println("Thread UDPclient started"); 
	         
	            //String tab = login ;
	            //byte[] buffer = tab.getBytes();
	            
	            try {
	               //On initialise la connexion c�t� client
	               DatagramSocket client = new DatagramSocket();
	               
	               Message m = new Message(login, 0);
	               ByteArrayOutputStream ArrayStream = new ByteArrayOutputStream();
	               try {
	                   ObjectOutputStream ObjectStream = new ObjectOutputStream(ArrayStream);
	                   ObjectStream.writeObject(m);
	               } catch(IOException e) {
	                   System.err.println("Erreur lors de la s�rialisation : " + e);
	                   System.exit(-1);
	               }
	              
	               byte[] buffer = ArrayStream.toByteArray();
	               
	               //On cr�e notre datagramme
	               InetAddress adresse = InetAddress.getByName("255.255.255.255");
	               DatagramPacket outpacket = new DatagramPacket(buffer, buffer.length, adresse, 2000);
	               
	               //On lui affecte les donn�es � envoyer
	               outpacket.setData(buffer);
	               
	               //On envoie au serveur
	               client.send(outpacket);
	               
	               //Et on r�cup�re la r�ponse du serveur
	               byte[] buffer2 = new byte[1024];
	               DatagramPacket inpacket = new DatagramPacket(buffer2, buffer2.length);
	               
	               long time = System.currentTimeMillis();
	               long fin=time + 5000;
	               Message m2=new Message("");
	               
	               while(fin > System.currentTimeMillis()) {
	            	   
	            	   client.receive(inpacket);
	            	   
	            	   ByteArrayInputStream ArrayStream2 = new ByteArrayInputStream(inpacket.getData());
	                   ObjectInputStream ObjectStream2 = new ObjectInputStream(ArrayStream2);
	                   try {
						m2 = (Message) ObjectStream2.readObject();
	                   } catch (ClassNotFoundException e) {
						System.exit(-1);
						e.printStackTrace();
	                   }
		              
		               if(m2.getType()==1) {
		            	   //update base de donn�e local = ajout d'un user connect�
		               }
		               else if(m2.getType()==-1){
		            	   //login d�j� utilis�
		               }
		               buffer2=new byte[1024];
		               
	               }         
	               
	               client.close();
	                            
	            } catch (SocketException e) {
	               e.printStackTrace();
	            } catch (UnknownHostException e) {
	               e.printStackTrace();
	            } catch (IOException e) {
	               e.printStackTrace();
	            }
	         }
	        
	   }   
	
	public static class UDPclientDisconnect implements Runnable{
		
		User user;
		
		public UDPclientDisconnect(String pName, User utilisateur){
	         this.user=utilisateur;
	      }
		
		public void run() {
			System.out.println("Thread Disconnect started");
			try {
	               //On initialise la connexion c�t� client
	               DatagramSocket client = new DatagramSocket();
	               
	               Message m = new Message(this.user.getLogin(), 2);
	               ByteArrayOutputStream ArrayStream = new ByteArrayOutputStream();
	               try {
	                   ObjectOutputStream ObjectStream = new ObjectOutputStream(ArrayStream);
	                   ObjectStream.writeObject(m);
	               } catch(IOException e) {
	                   System.err.println("Erreur lors de la s�rialisation : " + e);
	                   System.exit(-1);
	               }
	              
	               byte[] buffer = ArrayStream.toByteArray();
	               
	               //On cr�e notre datagramme
	               InetAddress adresse = InetAddress.getByName("255.255.255.255");
	               DatagramPacket outpacket = new DatagramPacket(buffer, buffer.length, adresse, 2000);
	               
	               //On lui affecte les donn�es � envoyer
	               outpacket.setData(buffer);
	               
	               //On envoie au serveur
	               client.send(outpacket);
	               
	               client.close();
	               
				} catch (SocketException e) {
	               e.printStackTrace();
	            } catch (UnknownHostException e) {
	               e.printStackTrace();
	            } catch (IOException e) {
	               e.printStackTrace();
	            }
		}
	}
	
	//  Thread ClientBroadcast = new Thread(new UDPclientBroadcast("Donn�es",1000)); 
}