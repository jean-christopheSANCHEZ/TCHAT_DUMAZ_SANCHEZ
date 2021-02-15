package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import clientClavardage.Conversation;
import clientClavardage.DatabaseConv_mess;
import clientClavardage.TCPconvInit;
import clientClavardage.TCPconvInit.TCPstartconv;
import clientLogin.DatabaseLogin;
import clientLogin.User;
import clientLogin.UDPBroadcast;

//c'est la page principale du client
public class MainFrame {
	private JFrame frame = new JFrame("Second");
	
	public MainFrame(User user) {
		//init du server udp pour recevoir les broadcast
		Thread udpserver = new Thread(new UDPBroadcast.UDPserver(user.getLogin(), user));
		udpserver.start();
		
		//init du server tcp pour recevoir les messages
		Thread tcpserver = new Thread(new TCPconvInit.TCPserverconv(user));
		tcpserver.start();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1800,1000);
		//on met le titre de la page avec le login du l utilisateur connecté
		frame.setTitle(user.getLogin()+" connected on port : " +user.getNumPort());
		
		
		Container contentPane = frame.getContentPane();
	    
	    
	    Container contentListConv = frame.getContentPane();
	    

	    //Add the buttons.
	    JButton logOutButton = new JButton("Log Out");
	    
	    
	    JPanel panel = new JPanel(new GridLayout(1, 3));    
	   
	    
	    panel.add(logOutButton);
	    
	    // déclare la list qui va contenir les conv
	    JPanel panelList = new JPanel(new GridLayout(2, 1));
	    
	    
	    //bouton d'actualisation
	    JButton actualiseListConv = new JButton("Refresh conversation list");
	    panel.add(actualiseListConv);
	    
	    JList listConv = new JList();
	    actualiseListConv.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
							
				try {
				//mise en place de la bb pour rechercher toutes les conv qui contiennent notre login	
				ResultSet result = null;
				DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), null, -1);
				DB.selectConvToMainUser();
				result = DB.getResult();
				
				//récupération et utilisation du resultat retourné par la base de donnée
				List<String> strings = new ArrayList<String>();
				
				String stringInfo = null;
				ResultSetMetaData rsmd = result.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				   while (result.next()) {
					   stringInfo = "";
					   for (int i = 1; i <= columnsNumber; i++) {
				           
				           String columnValue = result.getString(i);
				           //System.out.print(rsmd.getColumnName(i) + " : " + columnValue);
				           stringInfo = stringInfo + rsmd.getColumnName(i) + ":" + columnValue + ":   ";
				           
				           
				       }
					   strings.add(stringInfo);
				       System.out.println("");
				   } 
				   System.out.println(strings);
				   
				   
				   String[] tabConv = new String[strings.size()];
				   tabConv = strings.toArray(tabConv);
				   
				   listConv.setModel(new AbstractListModel() {
			            @Override
			            public int getSize() {
			                return strings.size();
			            }

			            @Override
			            public Object getElementAt(int i) {
			                return strings.get(i);
			            }
			        });
				   
				   //sur le clic de la conversation on fait tout ca
				   listConv.addListSelectionListener(new ListSelectionListener() {

			            @Override
			            public void valueChanged(ListSelectionEvent evt) {
			            	//on recupere le text de la conversation sur laquelle on liquer puis on parse pour recuperer ce qui nous interesse
			            	String s = (String) listConv.getSelectedValue();
			            	String sTab [] = s.split(":");
			            	
			            	User destinataire;
			            	
			            	DatabaseLogin DB = new DatabaseLogin(user.getLogin(), user.getNumPort());
			            	
			            	if(sTab[3].equals(user.getLogin())) {
			            		DB.selectUserByLogin(sTab[5]);
								ResultSet result2 = DB.getResult();
								try {
									if(result2.next()) {
										//on creer un user destinataire pour creer une variable conversatio car on en a besoin ensuite pour tcp
										destinataire = new User(sTab[5],Integer.parseInt(result2.getString(1)) ,InetAddress.getLocalHost(),Integer.parseInt(result2.getString(3)) );
										Conversation conv = new Conversation(user, destinataire, Integer.parseInt(sTab[1]));
										//on appelle statconv qui va initier la conversation tcp et ouvrir les fenetres de dialogue sur les deux clients (destinataire et le notre)
										TCPstartconv startConv = new TCPstartconv(user, destinataire, conv);
										startConv.init();
										listConv.clearSelection();
									}else {
										System.out.println("utilisateur pas connecté");
									}
								} catch (NumberFormatException | UnknownHostException | SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			            	}
			            	else{
			            		DB.selectUserByLogin(sTab[3]);
								ResultSet result2 = DB.getResult();
								try {
									if(result2.next()) {
										destinataire = new User(sTab[3],Integer.parseInt(result2.getString(1)) ,InetAddress.getLocalHost(),Integer.parseInt(result2.getString(3)) );
										Conversation conv = new Conversation(user, destinataire, Integer.parseInt(sTab[1]));
										TCPstartconv startConv = new TCPstartconv(user, destinataire, conv);
										startConv.init();
										listConv.clearSelection();
									}else {
										System.out.println("utilisateur pas connecté");
									}
								} catch (NumberFormatException | UnknownHostException | SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			            	}
            				
					       DB.deconnect();
			            	
			            }
			        });
				   
				   DB.deconnect();
				   
				   
				   frame.repaint();
				   frame.setVisible(true);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	    
	    //on quit l application on aire la fenetre et on vide la base de donnée locale des login
	    logOutButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				Login newLogPage = new Login();
				frame.dispose();
				
				DatabaseLogin DB = new DatabaseLogin(user.getLogin(),user.getNumPort());
				DB.deleteAllField();
				DB.deconnect();
				
				
			}
		});
	    
	    
	    JButton newconv = new JButton("New conversation");
	    panel.add(newconv);
	    newconv.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				//on appelle la classe qui s'occupe de faire la nouvelle conv
	    		new NewConversationForm(user);
	    		
			}
		});
	    
	    panelList.add(listConv);
	    contentPane.add(panel, BorderLayout.NORTH);
	    contentListConv.add(panelList, BorderLayout.CENTER);
	    
	    
	    frame.setLocationRelativeTo(null);
	    frame.repaint();
	    frame.setVisible(true);
	       
	}
}
