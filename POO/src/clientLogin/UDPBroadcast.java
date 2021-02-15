package clientLogin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.SQLException;

import clientClavardage.Message;

//ensemble des fonctions relatives � la phase de connexion via UDP
public class UDPBroadcast {
	
	//serveur UDP qui tourne tant que l'utilisateur est connect�
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
	//thread charg� de r�pondre aux demandes de connexion d'autres utilisateurs
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
			//v�rifie si le message provient d'un utilisateur faisant une demande en broadcast (type=0) 
			if(m.getType()==0) {
					
					if(m.getData().equals(this.user.getLogin())) {
						//le login est le m�me il doit donc renvoyer un message avec type=-1
						m2.setData(this.user.getLogin());
						m2.setType(-1);
					}else {
						//le login est diff�rent donc type=1
						m2.setData(this.user.getLogin());
						m2.setType(1);
					}
			//envoie la r�ponse					
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
			//si type=2 alors le message provient d'un utilisateur se d�connectant
	   		else if(m.getType()==2) {
	   			
			// retirer de la base de donn�es local
	   		DB.deleteByLogin(m.getData());
	   			
			}
			DB.deconnect();
			
		}
	}

	//premi�re version de la fonction charg�e d'envoyer la demande en broadcast ; la version finale est UDPclientBroadcast2  
	public static class UDPclientBroadcast implements Runnable{
	      String login = "";	      
	      User user;
	      
	      public UDPclientBroadcast(String pName, User utilisateur){
	         this.login = utilisateur.getLogin();
	         this.user=utilisateur;
	      }
	      
	      public void run(){
	    	 System.out.println("Thread UDPclient started"); 
	         
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
	
	
	//version finale de la fonction charg�e d'envoyer en broadcast � tous les autres utilisateurs une demande de connexion
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
	            //ici puisqu'on test en local sur diff�rents ports on cr�e plusieurs datagramme que l'on envoie sur 3 ports de r�f�rence utilis�s pour le test
	            // si on �tait dans un r�seau local d'entreprise on utiliserait directement un seul port de r�f�rence de l'application et on ferait un broacast sur la couche IP
	            InetAddress adresse = InetAddress.getLocalHost();
	            DatagramPacket outpacket = new DatagramPacket(buffer, buffer.length, adresse, 2000);
	            DatagramPacket outpacket2 = new DatagramPacket(buffer, buffer.length, adresse, 3000);
	            DatagramPacket outpacket3 = new DatagramPacket(buffer, buffer.length, adresse, 4000);
	            //On lui affecte les donn�es � envoyer
	            outpacket.setData(buffer);
	               
	            System.out.println("Envoie du broadcast");
	            //On envoie au(x) serveur(s)
	            client.send(outpacket);
	            client.send(outpacket2);
	            client.send(outpacket3);
	            
	            //Et on r�cup�re les r�ponses du serveur  
	            Message m2=new Message("");
	            
	            //on d�finit une p�riode de 4s pour recevoir des r�ponses d'autres utilisateurs
	            //au del� on consid�re que tous les utilisateurs nous ont d�j� repondu
	            client.setSoTimeout(4000);
	            while(true/*fin > System.currentTimeMillis()*/) {
	            	byte[] buffer2 = new byte[1024];
		            DatagramPacket inpacket = new DatagramPacket(buffer2, buffer2.length);
	            	System.out.println("Attente des r�ponses ...");
	            	client.receive(inpacket);
	            	System.out.println("Une r�ponse recu !" + inpacket.getPort());
	            	ByteArrayInputStream ArrayStream2 = new ByteArrayInputStream(inpacket.getData());
	                ObjectInputStream ObjectStream2 = new ObjectInputStream(ArrayStream2);
	                try {
	                m2 = (Message) ObjectStream2.readObject();
	                } catch (ClassNotFoundException e) {
	                	System.exit(-1);
						e.printStackTrace();
	                }
	                
		            //on analyse les r�ponses en fonctions du champ type dans le message
	                
	                //type=1 r�ponse positive
		            if(m2.getType()==1) {   	   
		            	DatabaseLogin DB = new DatabaseLogin(m2.getData(),inpacket.getPort());
		            	DB.insertLoginPort();
		            	DB.deconnect();
		            	   
		            	//update base de donn�e local = ajout d'un user connect�
		            }
		            else if(m2.getType()==-1){
		            	//type=-1 r�ponse n�gative login d�j� utilis�
		            	//il suffit d'une r�ponse n�gative pour refuser la connexion
		            state=-1;
		            System.out.println("erreur login d�j� utilis�");
		            	   //vide la base de donn�es
		            } 
		               
	               }         
	               
	                            
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
	
	
	//fonction charg�e d'envoyer un message de d�connexion aux autres utilisateurs
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

}