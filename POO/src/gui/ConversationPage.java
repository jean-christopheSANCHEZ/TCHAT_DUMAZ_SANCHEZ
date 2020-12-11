package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.SpringLayout;

import clientClavardage.Conversation;
import clientLogin.User;

public class ConversationPage extends JFrame implements ActionListener{
	
	
	public ConversationPage(Conversation conv) {
		
		JFrame frame = new JFrame("New conv");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    Container contentPane = frame.getContentPane();
	    
	    
	    
	    JPanel panelBas = new JPanel(new GridLayout(3,1));
	    JPanel panelHaut = new JPanel(new GridLayout(1,1));
	    
	    JLabel test = new JLabel("test");
	    panelHaut.add(test);
	    
	    
	    
	    JTextField newMessage = new JTextField("Enter new message");
	    panelBas.add(newMessage);
	    JButton send = new JButton("send message");
	    panelBas.add(send);
	    JButton back = new JButton("Back to Main Frame");
	    panelBas.add(back);
	    back.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				//save a conversation dans la bdd et ouvre la page de conversation
	    		//new MainFrame(user);
	    		frame.dispose();
			}
		});
	    
	    contentPane.add(panelHaut,BorderLayout.NORTH);
	    contentPane.add(panelBas,BorderLayout.SOUTH);
	    
	    frame.pack();
		frame.setSize(1200, 600);
		frame.setTitle("Conversation of " + conv.getUser1().getLogin() + " to " + conv.getUser2().getLogin() + " ID = "  + conv.getId());
	    frame.setVisible(true);
		
	}
	
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
