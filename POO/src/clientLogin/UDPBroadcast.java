package clientLogin;



import java.io.IOException;
import java.net.*;

public class UDPBroadcast {
	
	
	public static class UDPserver implements Runnable{
		String name="";
		long sleepTime=1000;
		
		public UDPserver(String Name, long sleep) {
			name=Name;
			sleepTime=sleep;
		}
		
		public void run() {
			
			while (true) {
				try {
			DatagramSocket serveur = new DatagramSocket(2000);
			
			
				}catch (SocketException e) {
		               e.printStackTrace();
		        } /*catch (UnknownHostException e) {
		               e.printStackTrace();
		        }*/ catch (IOException e) {
		               e.printStackTrace();
		        }
			}
		}
	}

	   
	public static class UDPclientBroadcast implements Runnable{
	      String name = "";
	      long sleepTime = 1000;
	      
	      public UDPclientBroadcast(String pName, long sleep){
	         name = pName;
	         sleepTime = sleep;
	      }
	      
	      public void run(){
	         int nbre = 0;
	         while(true){
	            String tab = name + "-" + (++nbre);
	            byte[] buffer = tab.getBytes();
	            
	            try {
	               //On initialise la connexion côté client
	               DatagramSocket client = new DatagramSocket();
	               
	               //On crée notre datagramme
	               InetAddress adresse = InetAddress.getLocalHost();
	               DatagramPacket outpacket = new DatagramPacket(buffer, buffer.length, adresse, 2000);
	               
	               //On lui affecte les données à envoyer
	               outpacket.setData(buffer);
	               
	               //On envoie au serveur
	               client.send(outpacket);
	               
	               //Et on récupère la réponse du serveur
	               byte[] buffer2 = new byte[256];
	               DatagramPacket inpacket = new DatagramPacket(buffer2, buffer2.length);
	               client.receive(inpacket);
	               
	               
	               
	               try {
	                  Thread.sleep(sleepTime);
	               } catch (InterruptedException e) {
	                  e.printStackTrace();
	               }
	               
	            } catch (SocketException e) {
	               e.printStackTrace();
	            } catch (UnknownHostException e) {
	               e.printStackTrace();
	            } catch (IOException e) {
	               e.printStackTrace();
	            }
	         }
	      }      
	   }   
	  Thread ClientBroadcast = new Thread(new UDPclientBroadcast("Données",1000));
}

