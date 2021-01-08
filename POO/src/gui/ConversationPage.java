package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

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
	    
	    Thread tcpServer = new Thread(new TCPconvInit.TCPserverconv(user));
		tcpServer.start();
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
	    //JPanel panelHautReception = new JPanel(new GridLayout(1,1));
	    //JPanel panelHautEnvoie = new JPanel(new GridLayout(1,1));
	    
	    
	    DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), conv.getUser2().getLogin(), conv.getUser2().getNumPort());
	    DB.selectListMessageById(conv.getId());
	    ResultSet result = DB.getResult();
	    JTextArea mess =new JTextArea();
	    String tmp  = new String();
	    try {
			while(result.next()) {
				
				/*JLabel mess = new JLabel( tmp + "<html> test<br></html>");*/
				//String labelText ="<html>"+tmp+"</html>";
				
				
				
				if(result.getString(4).equals(user.getLogin())) {
					
					tmp = result.getString(2) +" : " + result.getString(3) +"\n";
					mess.append(tmp);
					//mess.setForeground(Color.blue);
					//mess.setLocation(200,300);
					//panelHautEnvoie.add(mess);
					
					/*panelHautEnvoie.revalidate();
					panelHautEnvoie.repaint();
					frame.repaint();*/
				}else {
					
					tmp = "                                                                                                                                                " +result.getString(2) +" : " + result.getString(3) +"\n";
					mess.append(tmp);
					//mess.setForeground(Color.green);
					//mess.setLocation(50,300);
					//panelHautReception.add(mess);
					/*panelHautReception.revalidate();
					panelHautReception.repaint();
					frame.repaint();*/
				
				}
				//panelHaut.add(panelHautEnvoie);
				//panelHaut.add(panelHautReception);
				
				panelHaut.add(mess);
			    System.out.println(mess.getText());
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
	    		Message newMess = new Message(newMessage.getText(), user, conv.getUser2());
				/*DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), conv.getUser2().getLogin(), conv.getUser2().getNumPort());
				DB.insertMessage(newMess, conv.getId(), conv.getUser1());
				DB.deconnect();*/
	    		Thread tcpsendmessage = new Thread(new TCPconvInit.TCPstartconv(conv.getUser2(), newMess));
	    		tcpsendmessage.start();           
	    		String tmp2 = new String();
	    		tmp2 = newMess.getDateEnvoie() +" : " + newMess.getData()+"\n";
				mess.append(tmp2);
	    		
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
