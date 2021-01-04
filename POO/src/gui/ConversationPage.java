package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.SpringLayout;

import clientClavardage.Conversation;
import clientClavardage.DatabaseConv_mess;
import clientClavardage.Message;
import clientClavardage.TCPconvInit;
import clientLogin.User;

public class ConversationPage extends JFrame implements ActionListener{
	
	
	public ConversationPage(Conversation conv, User user) {
		
		JFrame frame = new JFrame("New conv");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    Container contentPane = frame.getContentPane();
	    
	    
	    User destinataire;
	    if(user.getLogin().equals(conv.getUser2().getLogin())) {
	    	destinataire = conv.getUser1();
	    	System.out.println(conv.getUser1().getLogin() + conv.getUser2().getLogin());
	    	System.out.println(user.getLogin() + "1 dest : " + destinataire.getLogin());
	    }else {
	    	destinataire = conv.getUser2();
	    	System.out.println(conv.getUser1().getLogin() + conv.getUser2().getLogin());
	    	System.out.println(user.getLogin() + "2 dest : " + destinataire.getLogin());
	    }
	    
	    
	    
	    
	    
	    JPanel panelBas = new JPanel(new GridLayout(3,1));
	    JPanel panelHaut = new JPanel(new GridLayout(1,1));
	    
	    DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), conv.getUser2().getLogin(), conv.getUser2().getNumPort());
	    DB.selectListMessageById(conv.getId());
	    ResultSet result = DB.getResult();
	    
	    try {
			while(result.next()) {
				JLabel mess = new JLabel(result.getString(2) +" : " + result.getString(3));
				if(result.getString(4).equals(user.getLogin())) {
					mess.setForeground(Color.blue);
				}else {
					mess.setForeground(Color.green);
				}
				panelHaut.add(mess);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    DB.deconnect();
	    
	    
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
	    
	    send.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
	    		Message newMess = new Message(newMessage.getText(), user);
				/*DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), conv.getUser2().getLogin(), conv.getUser2().getNumPort());
				DB.insertMessage(newMess, conv.getId(), conv.getUser1());
				DB.deconnect();*/
	    		Thread tcpsendmessage = new Thread(new TCPconvInit.TCPstartconv(conv.getUser2(), newMess));
	    		tcpsendmessage.start();
				
	    		//trouve l'envoyeur du message
            	// faire une recherche dans BDD conv si conv existe ajout msg sinon ajout conv puis ajout msg
            	DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), conv.getUser2().getLogin(), conv.getUser2().getNumPort());
            	DB.selectConv(user, conv.getUser2());
            	ResultSet result = DB.getResult();            	
            	System.out.println(result);
            	DB.deconnect();
            
	    		
	    		
			}
		});
	    
	    contentPane.add(panelHaut,BorderLayout.NORTH);
	    contentPane.add(panelBas,BorderLayout.SOUTH);
	    
	    frame.pack();
		frame.setSize(1200, 600);
		frame.setTitle("Conversation of " + user.getLogin() + " to " + destinataire.getLogin() + " ID = "  + conv.getId());
	    frame.setVisible(true);
		
	}
	
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
