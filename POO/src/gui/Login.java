package gui;



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
       
       panel = new JPanel(new GridLayout(3, 1));
       
       panel.add(user_label);
       panel.add(userName_text);
       panel.add(port_label);
       panel.add(userPort);
       panel.add(logButton);
       add(panel, BorderLayout.CENTER);
       
       
       contentPane.add(panel);
       
       
       logButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String userName = userName_text.getText();
			    int portNumber= Integer.parseInt(userPort.getText()); 
			    User newUtilisateur;
				try {
					newUtilisateur = new User(userName, 1, InetAddress.getLocalHost(), portNumber);
					frame.dispose();
					new MainFrame(newUtilisateur);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
       
	   frame.pack();
	   frame.setSize(300, 600);
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