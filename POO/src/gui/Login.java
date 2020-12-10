package gui;


import java.sql.*;
import clientLogin.*;

import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
public class Login extends JFrame implements ActionListener {
   public User newUser; 
   
   JPanel panel;
   JLabel user_label, message, port_label;
   JTextField userName_text, userPort;
   //JPasswordField password_text;
   JButton submit, cancel;
   Login() {
	 //Create and set up the window.
       JFrame frame = new JFrame("Tchat");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
       
     //Set up the content pane.
       Container contentPane = frame.getContentPane();
       contentPane.setLayout(new SpringLayout());

       //Add the buttons.
       JButton logButton = new JButton("Log In");

       
       
       user_label = new JLabel();
       user_label.setText("Login :");
       userName_text = new JTextField();
       
       port_label = new JLabel();
       port_label.setText("Port number :");
       userPort = new JTextField();
       
       JLabel errorConnectionMessage = new JLabel();
       errorConnectionMessage.setText("");
       
       
       panel = new JPanel(new GridLayout(3, 2));
       
       panel.add(user_label);
       panel.add(userName_text);
       panel.add(port_label);
       panel.add(userPort);
       panel.add(logButton);
       panel.add(errorConnectionMessage);
       
       
       
       
       
       
       logButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				String userName = userName_text.getText();
			    int portNumber= Integer.parseInt(userPort.getText()); 
			    User newUtilisateur;
				/*try {
					
					
					Class.forName("com.mysql.cj.jdbc.Driver");
					
					String DBurl = "jdbc:mysql://localhost/login?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
					Connection con = DriverManager.getConnection(DBurl, "root", "");
					ResultSet result = null;
					String requete = "SELECT identifiant,port FROM user WHERE identifiant='"+userName+"' AND port='"+portNumber+"'";
					Statement stmt = con.createStatement();
					result = stmt.executeQuery(requete);
					
					String bddResultLogin = null,bddResultPort = null;
					
					ResultSetMetaData rsmd = result.getMetaData();
					int columnsNumber = rsmd.getColumnCount();
					   while (result.next()) {
					       for (int i = 1; i <= columnsNumber; i++) {
					           if (i == 1) {
					        	   bddResultLogin = result.getString(i);
					        	   }
					           if (i == 2) {
					        	   bddResultPort = result.getString(i);
					        	   }
					       }
					   }
					   
					   System.out.println(bddResultLogin + " port : "+bddResultPort);   
					if(userName.equals(bddResultLogin) && portNumber == Integer.parseInt(bddResultPort)) {
						
						
						newUtilisateur = new User(userName, 1, InetAddress.getLocalHost(), portNumber);
						frame.dispose();
						new MainFrame(newUtilisateur);
					}else {
						System.out.println("Login issue : login or port number invalid");
						
						errorConnectionMessage.setText("Login issue: "+userName+" and "+portNumber+ " are invalid");
						errorConnectionMessage.setForeground(Color.RED);
						frame.repaint();
					}
					con.close();
					stmt.close();				
					
					
					
					
				} catch (UnknownHostException | ClassNotFoundException | SQLException e) {
					
					e.printStackTrace();
				}*/
			    
			    	
					try {
						DatabaseLogin DB = new DatabaseLogin(userName,portNumber);
						DB.insertLoginPort();
						DB.deconnect();
						
						
						newUtilisateur = new User(userName, 1, InetAddress.getLocalHost(), portNumber);
						frame.dispose();
						new MainFrame(newUtilisateur);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
				
				
				
				
			}
		});
       //setLayout(new BorderLayout());
       contentPane.add(panel/*,BorderLayout.NORTH*/);
       //contentPane.add(errorConnectionMessage/*, BorderLayout.SOUTH*/);
	   frame.pack();
	   frame.validate();
	   frame.setSize(400, 300);
	   frame.setTitle("LogIn");
       frame.setVisible(true);
	   
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      
   }
   public User getNewUser() {
	   return newUser;
   }
   
   
}