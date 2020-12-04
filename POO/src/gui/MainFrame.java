package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import clientLogin.User;

public class MainFrame {
	private JFrame frame = new JFrame("Second");
	
	public MainFrame(User user) {
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920,1080);
		frame.setTitle(user.getLogin()+" connected on port : " +user.getNumPort());
		
		
		
		Container contentPane = frame.getContentPane();
	    contentPane.setLayout(new SpringLayout());

	    //Add the buttons.
	    JButton logOutButton = new JButton("Log Out");
	    
	    JPanel panel = new JPanel(new GridLayout(3, 2));
	    panel.add(logOutButton);
	    
	    
	    logOutButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				Login newLogPage = new Login();
				frame.dispose();  
			}
		});
	    
	    
	    JButton newconv = new JButton("New conversation");
	    panel.add(newconv);
	    newconv.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				//save a conversation dans la bdd et ouvre la page de conversation
	    		new NewConversationForm(user);
			}
		});
	    
	    
	    
	    
	    //chercher la liste des conversation dans la bdd et les afficher toutes, les présenter sous la forme de lien qui àmene sur une nouvelle page pour envoyer le message et qui affiche tous les messages
	    
	    
	    contentPane.add(panel);
	    
	    frame.setVisible(true);
	       
	}
}
