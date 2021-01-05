package clientLogin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.SQLException;

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
			DatagramSocket serveur = new DatagramSocket(this.user.getNumPort());
			
			//byte[] buffer = new byte[1024];
			
			//DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
			
			while (true) {
				byte[] buffer = new byte[1024];
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
				serveur.receive(inPacket);
         	   	Thread response=new Thread(new UDPserverResponse(inPacket,this.user,serveur));
         	   	response.start();
			
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
	
	public static class UDPserverResponse implements Runnable {
		DatagramPacket inPacket;
		User user;
		DatagramSocket serveur;
		
		public UDPserverResponse(DatagramPacket packet,User utilisateur,DatagramSocket s) {
			this.inPacket=packet;
			this.user=utilisateur;
			this.serveur=s;
		}
		
		public void run() {
			Message m=new Message("");
			ByteArrayInputStream ArrayStream2 = new ByteArrayInputStream(inPacket.getData());
            ObjectInputStream ObjectStream2 = null;
			try {
				ObjectStream2 = new ObjectInputStream(ArrayStream2);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
            	m = (Message) ObjectStream2.readObject();
            } catch (ClassNotFoundException e) {
					System.exit(-1);
					e.printStackTrace();
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			InetAddress clientAddress = inPacket.getAddress();
			int clientPort = inPacket.getPort();
			
			DatabaseLogin DB = new DatabaseLogin(m.getData(),clientPort);
			Message m2=new Message("",-1);
			
			if(m.getType()==0) {
				//try {
				/*DB.selectUserByLogin(m.getData());
				System.out.println(m.getData());
				System.out.println(DB.getResult().next());
				
					if(DB.getResult().next()) {
					m2.setData(this.user.getLogin());
					m2.setType(-1);
					}
					else{
						m2.setData(this.user.getLogin());
						m2.setType(1);
						DB.insertLoginPort();
						
					}*/
					
					if(m.getData().equals(this.user.getLogin())) {
						m2.setData(this.user.getLogin());
						m2.setType(-1);
					}else {
						m2.setData(this.user.getLogin());
						m2.setType(1);
					}
				//} catch (SQLException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				//}
					
					
					
				ByteArrayOutputStream ArrayStream = new ByteArrayOutputStream();
	            try {
	            	ObjectOutputStream ObjectStream = new ObjectOutputStream(ArrayStream);
	                ObjectStream.writeObject(m2);
	            } catch(IOException e) {
	                System.err.println("Erreur lors de la s�rialisation : " + e);
	                System.exit(-1);
	            }
	              
	            byte[] buffer2 = ArrayStream.toByteArray();
	            DatagramPacket outPacket = new DatagramPacket(buffer2, buffer2.length, clientAddress, clientPort);
	   			try {
					serveur.send(outPacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	   			
			}
	   		else if(m.getType()==2) {
	   			
				// retirer de la base de donn�es local
	   			DB.deleteByLogin(m.getData());
	   			
			}
			DB.deconnect();
			
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
	               InetAddress adresse = InetAddress.getLocalHost();
	               DatagramPacket outpacket = new DatagramPacket(buffer, buffer.length, adresse, 2000);
	               
	               //On lui affecte les donn�es � envoyer
	               outpacket.setData(buffer);
	               
	               //On envoie au serveur
	               client.send(outpacket);
	               
	               //Et on r�cup�re la r�ponse du serveur
	               byte[] buffer2 = new byte[1024];
	               DatagramPacket inpacket = new DatagramPacket(buffer2, buffer2.length);
	               
	               long time = System.currentTimeMillis();
	               long fin=time + 2000;
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
		            	   
		            	   DatabaseLogin DB = new DatabaseLogin(m2.getData(),0);
		            	   DB.insertLoginPort();
		            	   DB.deconnect();
		            	   
		            	   //update base de donn�e local = ajout d'un user connect�
		               }
		               else if(m2.getType()==-1){
		            	   //login d�j� utilis�
		            	  
		            	   System.out.println("erreur login d�j� utilis�");
		            	   //vide la base de donn�es
		               }
		               buffer2=new byte[1024];
		               
	               }         
	               
	               client.close();
	               notifyAll();
	                            
	            } catch (SocketException e) {
	               e.printStackTrace();
	            } catch (UnknownHostException e) {
	               e.printStackTrace();
	            } catch (IOException e) {
	               e.printStackTrace();
	            }

	         }
	        
	   }
	
	
	
	public static class UDPclientBroadcast2{
	      String login = "";	      
	      User user;
	      
	      public UDPclientBroadcast2(String pName, User utilisateur){
	         this.login = utilisateur.getLogin();
	         this.user=utilisateur;
	      }
	      
	     public int executeBroadcast() {
	    	 System.out.println("Thread UDPclient started"); 
	         int state = 0;
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
	               InetAddress adresse = InetAddress.getLocalHost();
	               DatagramPacket outpacket = new DatagramPacket(buffer, buffer.length, adresse, 2000);
	               DatagramPacket outpacket2 = new DatagramPacket(buffer, buffer.length, adresse, 3000);
	               DatagramPacket outpacket3 = new DatagramPacket(buffer, buffer.length, adresse, 4000);
	               //On lui affecte les donn�es � envoyer
	               outpacket.setData(buffer);
	               
	               System.out.println("Envoie du broadcast");
	               //On envoie au serveur
	               client.send(outpacket);
	               client.send(outpacket2);
	               client.send(outpacket3);
	               //Et on r�cup�re la r�ponse du serveur
	               //byte[] buffer2 = new byte[1024];
	               //DatagramPacket inpacket = new DatagramPacket(buffer2, buffer2.length);
	               
	               /*long time = System.currentTimeMillis();
	               long fin=time + 5000;*/
	               Message m2=new Message("");
	               
	               client.setSoTimeout(4000);
	               while(true/*fin > System.currentTimeMillis()*/) {
	            	   byte[] buffer2 = new byte[1024];
		               DatagramPacket inpacket = new DatagramPacket(buffer2, buffer2.length);
	            	   System.out.println("Attente des r�ponses ...");
	            	   client.receive(inpacket);
	            	   System.out.println("Une r�ponse re�u !" + inpacket.getPort());
	            	   ByteArrayInputStream ArrayStream2 = new ByteArrayInputStream(inpacket.getData());
	                   ObjectInputStream ObjectStream2 = new ObjectInputStream(ArrayStream2);
	                   try {
						m2 = (Message) ObjectStream2.readObject();
	                   } catch (ClassNotFoundException e) {
						System.exit(-1);
						e.printStackTrace();
	                   }
		              
		               if(m2.getType()==1) {
		            	   
		            	   DatabaseLogin DB = new DatabaseLogin(m2.getData(),inpacket.getPort());
		            	   DB.insertLoginPort();
		            	   DB.deconnect();
		            	   
		            	   //update base de donn�e local = ajout d'un user connect�
		               }
		               else if(m2.getType()==-1){
		            	   //login d�j� utilis�
		            	  state=-1;
		            	   System.out.println("erreur login d�j� utilis�");
		            	   //vide la base de donn�es
		               }
		               //buffer2=new byte[1024];
		               
	               }         
	               
	               //client.close();

	                            
	            } catch (SocketException e) {
	               e.printStackTrace();
	            } catch (UnknownHostException e) {
	               e.printStackTrace();
	            } catch (IOException e) {
	               e.printStackTrace();
	            }
	            return state;
	        
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