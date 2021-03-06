package gui;


import java.sql.*;
import clientLogin.*;
import clientLogin.UDPBroadcast.UDPclientBroadcast2;

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
       
       
       
       
       
       //sur le clique du bouton
       logButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				String userName = userName_text.getText();
			    int portNumber= Integer.parseInt(userPort.getText()); 
			    User newUtilisateur;
		    	
					try {
						//broadcast udp
						int state = 0;
						newUtilisateur = new User(userName, 1, InetAddress.getLocalHost(), portNumber);
						UDPclientBroadcast2 udpbroadcast = new UDPclientBroadcast2(newUtilisateur.getLogin(), newUtilisateur);
						state = udpbroadcast.executeBroadcast();
						
						if(state ==0) {
							//on ajoute notre login a la bdd locale
							DatabaseLogin DB = new DatabaseLogin(userName,portNumber);
							DB.insertLoginPort();
							DB.deconnect();

							frame.dispose();
							new MainFrame(newUtilisateur);
						}else {
							errorConnectionMessage.setText("Login issue : type an other login ");
							errorConnectionMessage.setForeground(Color.RED);
							frame.repaint();
							
						}
					
						
					} catch (UnknownHostException  e) {
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