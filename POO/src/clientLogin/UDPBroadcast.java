package clientLogin;

import java.io.IOException;
import java.net.*;

public class UDPBroadcast {
	
	
	public static class UDPserver implements Runnable{
		String name="";
		User user;
		String response;
		
		public UDPserver(String Name, User utilisateur) {
			this.name=Name;
			this.user=utilisateur;
			this.response="";
			
		}
		
		public void run() {
			
				try {
					
			System.out.println("Thread ServeurUDP started");
			DatagramSocket serveur = new DatagramSocket(2000);
			
			byte[] buffer = new byte[256];
			
			DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
			
			while (true) {
			
			serveur.receive(inPacket);
			
			InetAddress clientAddress = inPacket.getAddress();
			int clientPort = inPacket.getPort();
			
			String message = new String(inPacket.getData(), 0, inPacket.getLength());
			
			response=this.user.getLogin();
			
			if(this.user.getLogin()!=message) {
			//update base de données local
			}
			
			DatagramPacket outPacket = new DatagramPacket(response.getBytes(), response.length(), clientAddress, clientPort);
			
			serveur.send(outPacket);
			
			}
			//serveur.close();
			
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
	         
	            String tab = login ;
	            byte[] buffer = tab.getBytes();
	            
	            try {
	               //On initialise la connexion côté client
	               DatagramSocket client = new DatagramSocket();
	               
	               //On crée notre datagramme
	               InetAddress adresse = InetAddress.getByName("255.255.255.255");
	               DatagramPacket outpacket = new DatagramPacket(buffer, buffer.length, adresse, 2000);
	               
	               //On lui affecte les données à envoyer
	               outpacket.setData(buffer);
	               
	               //On envoie au serveur
	               client.send(outpacket);
	               
	               //Et on récupère la réponse du serveur
	               byte[] buffer2 = new byte[256];
	               DatagramPacket inpacket = new DatagramPacket(buffer2, buffer2.length);
	               
	               long time = System.currentTimeMillis();
	               long fin=time + 5000;
	               
	               while(fin > System.currentTimeMillis()) {
	            	   
	            	   client.receive(inpacket);
		               
		               String response =new String(inpacket.getData(),0,inpacket.getLength());
		               
		               if (response==this.user.getLogin()) {
		            	   System.out.println("login déjà utilisé en ce moment");
		               }
		               
		               buffer2=new byte[256];
		               
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
			
		}
	}
	
	//  Thread ClientBroadcast = new Thread(new UDPclientBroadcast("Données",1000)); 
}