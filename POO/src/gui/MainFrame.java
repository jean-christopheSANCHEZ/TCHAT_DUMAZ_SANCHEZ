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
import clientLogin.DatabaseLogin;
import clientLogin.User;
import clientLogin.UDPBroadcast;

public class MainFrame {
	private JFrame frame = new JFrame("Second");
	
	public MainFrame(User user) {
		Thread udpserver = new Thread(new UDPBroadcast.UDPserver(user.getLogin(), user));
		udpserver.start();
		
		Thread tcpserver = new Thread(new TCPconvInit.TCPserverconv(user));
		tcpserver.start();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1800,1000);
		frame.setTitle(user.getLogin()+" connected on port : " +user.getNumPort());
		/*Thread tcpServer = new Thread(new TCPconvInit.TCPserverconv(user));
		tcpServer.start();*/
		
		
		Container contentPane = frame.getContentPane();
	    
	    
	    Container contentListConv = frame.getContentPane();
	    

	    //Add the buttons.
	    JButton logOutButton = new JButton("Log Out");
	    //JTextField rien = new JTextField(" ");
	    
	    JPanel panel = new JPanel(new GridLayout(1, 3));    
	   
	    
	    panel.add(logOutButton);
	    
	    
	    JPanel panelList = new JPanel(new GridLayout(2, 1));
	    
	    
	    
	    JButton actualiseListConv = new JButton("Refresh conversation list");
	    panel.add(actualiseListConv);
	    
	    JList listConv = new JList();
	    actualiseListConv.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				

				/*DATABASE connection*/
				//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				try {/*
				Class.forName("com.mysql.cj.jdbc.Driver");
				String DBurl = "jdbc:mysql://localhost/conv_mess?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
				Connection con = DriverManager.getConnection(DBurl, "root", "");
				ResultSet result = null;
				String requete = "SELECT * FROM conversation WHERE User1 = '" + user.getLogin() +"' or User2 = '" + user.getLogin() +"'";
				Statement stmt = con.createStatement();
				result = stmt.executeQuery(requete);*/
				
					
				ResultSet result = null;
				DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), null, -1);
				DB.selectConvToMainUser();
				result = DB.getResult();
				
				
				List<String> strings = new ArrayList<String>();
				
				String stringInfo = null;
				ResultSetMetaData rsmd = result.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				   while (result.next()) {
					   stringInfo = "";
					   for (int i = 1; i <= columnsNumber; i++) {
				           //if (i > 1) System.out.print(",  "); 
				           String columnValue = result.getString(i);
				           //System.out.print(rsmd.getColumnName(i) + " : " + columnValue);
				           stringInfo = stringInfo + rsmd.getColumnName(i) + ":" + columnValue + ":   ";
				           
				           
				       }
					   strings.add(stringInfo);
				       System.out.println("");
				   } 
				   System.out.println(strings);
				   
				   //JList listConv = new JList();
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
				   
				   
				   
				   
				   listConv.addListSelectionListener(new ListSelectionListener() {

			            @Override
			            public void valueChanged(ListSelectionEvent evt) {
			            	String s = (String) listConv.getSelectedValue();
			            	//System.out.println(s);
			            	String sTab [] = s.split(":");
			            	//System.out.println(sTab[1] + "   "+ sTab[3] + "  " +sTab[5]);
			            	DatabaseLogin DB = new DatabaseLogin(user.getLogin(), user.getNumPort());
			            	DB.selectUserByLogin(sTab[5]);
			            	ResultSet result = DB.getResult();
			            	try {
			            		if(result.next()) {
			            			System.out.println("conv page open");
			            			User destinataire;
			            			//System.out.println(result.getString(1) + "   "+ result.getString(2) + "  " +result.getString(3));
			            			if(user.getLogin().equals(sTab[5])){
			            				destinataire = new User(sTab[3],Integer.parseInt(result.getString(1)) ,InetAddress.getLocalHost(),Integer.parseInt(result.getString(3)) );
				   					}else {
				   						destinataire = new User(sTab[5],Integer.parseInt(result.getString(1)) ,InetAddress.getLocalHost(),Integer.parseInt(result.getString(3)) );
				   					}
					            	Conversation conv = new Conversation(user, destinataire, Integer.parseInt(sTab[1]));
					            	/*new ConversationPage(conv, user);*/
					            	Thread startConv = new Thread(new TCPconvInit.TCPstartconv(user, destinataire, conv));
					            	startConv.start();
					            	
					            	DB.deconnect();
			            		}
								
							} catch (SQLException | NumberFormatException | UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	
			            }
			        });
				   
				   DB.deconnect();
				   //panel.add(listConv);
				   
				   frame.repaint();
				   frame.setVisible(true);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	    
	    
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
				//save a conversation dans la bdd et ouvre la page de conversation
	    		new NewConversationForm(user);
	    		//frame.dispose();
			}
		});
	    
	    
	    
	   
	    
	    /*JList listConv = new JList();
	    
	    listConv.setModel(new AbstractListModel() {

            String[] strings = { }
            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public Object getElementAt(int i) {
                return strings[i];
            }
        });
	    
	    
	    listConv.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent evt) {
                new ConversationPage(user);
                //frame.dispose();
            }
        });
	    */
	    
	    //panel.add(listConv);
	    panelList.add(listConv);
	    //panelList.add(rien);
	    contentPane.add(panel, BorderLayout.NORTH);
	    contentListConv.add(panelList, BorderLayout.CENTER);
	    
	    
	    frame.setLocationRelativeTo(null);
	    frame.repaint();
	    frame.setVisible(true);
	       
	}
}
