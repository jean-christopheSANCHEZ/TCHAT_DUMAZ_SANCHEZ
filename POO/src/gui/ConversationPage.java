package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import clientClavardage.Conversation;
import clientLogin.User;

public class ConversationPage extends JFrame implements ActionListener{
	
	private JPanel panel;
	
	
	public ConversationPage(/*Conversation conv ,*/User user) {
		
		JFrame frame = new JFrame("New conv");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    Container contentPane = frame.getContentPane();
	    contentPane.setLayout(new SpringLayout());
	    
	    
	    panel = new JPanel(new GridLayout(2, 1));
	    add(panel, BorderLayout.CENTER);
	    
	    
	    JButton back = new JButton("Back to Main Frame");
	    panel.add(back);
	    back.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				//save a conversation dans la bdd et ouvre la page de conversation
	    		new MainFrame(user);
	    		frame.dispose();
			}
		});
	    
	    
	    contentPane.add(panel);
	    
	    frame.pack();
		frame.setSize(1200, 600);
		frame.setTitle("Conversation of " + user.getLogin() + " to " + /*variable login dest*/ " ID = "  /*id de la conv*/);
	    frame.setVisible(true);
		
	}
	
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
