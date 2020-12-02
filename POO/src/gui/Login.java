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
   JLabel user_label, password_label, message, port_label;
   JTextField userName_text, userPort;
   //JPasswordField password_text;
   JButton submit, cancel;
   Login() {
      // Username Label
      user_label = new JLabel();
      user_label.setText("User Name :");
      userName_text = new JTextField();
      port_label = new JLabel();
      port_label.setText("Enter your port number for connection :");
      userPort = new JTextField();
      // Password Label
      //password_label = new JLabel();
      //password_label.setText("Password :");
      //password_text = new JPasswordField();
      // Submit
      submit = new JButton("SUBMIT");
      panel = new JPanel(new GridLayout(3, 1));
      panel.add(user_label);
      panel.add(userName_text);
      panel.add(port_label);
      panel.add(userPort);
      //panel.add(password_label);
      //panel.add(password_text);
      message = new JLabel();
      panel.add(message);
      panel.add(submit);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Adding the listeners to components..
      submit.addActionListener(this);
      add(panel, BorderLayout.CENTER);
      setTitle("Please Login Here !");
      setSize(1920,1080);
      setVisible(true);
   }
   
   @Override
   public void actionPerformed(ActionEvent ae) {
      String userName = userName_text.getText();
      int portNumber= Integer.parseInt(userPort.getText());
      try {
		User newUtilisateur = new User(userName, 1, InetAddress.getLocalHost(), portNumber);
		newUser = newUtilisateur;
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      message.setText(" Hello " + userName + " you choose port : " + portNumber);
      this.setVisible(false);
      
   }
   public User getNewUser() {
	   return newUser;
   }
}