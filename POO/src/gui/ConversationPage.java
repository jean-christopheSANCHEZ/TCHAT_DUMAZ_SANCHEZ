package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import clientClavardage.Conversation;
import clientClavardage.DatabaseConv_mess;
import clientClavardage.Message;
import clientClavardage.TCPconvInit;
import clientClavardage.TCPconvInit.TCPmessage;
import clientLogin.User;

public class ConversationPage extends JFrame implements ActionListener{
	
	public JTextArea mess;
	private Socket link;
	
	public ConversationPage(Conversation conv, User user,Socket sock) {
		this.link = sock; //socket utilise pour l'envoie des messages
		JFrame frame = new JFrame("New conv");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    Container contentPane = frame.getContentPane();
	    
	    //mise a jour du destinataire car on fonction des cas il peut y avoir des changements, dans ces test on uniformalise et on est sur de notre destinataire
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
	    System.out.println(conv.getUser1().getNumPort() + conv.getUser2().getNumPort());
	    

	    JPanel panelBas = new JPanel(new GridLayout(3,1));
	    JPanel panelHaut = new JPanel(new GridLayout(1,1));
	  
	    // on selectionne dans la bdd les message qui correspondent à l'id de la conv (cle etrangere)
	    DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), conv.getUser2().getLogin(), conv.getUser2().getNumPort());
	    DB.selectListMessageById(conv.getId());
	    ResultSet result = DB.getResult();
	    this.mess =new JTextArea();
	    String tmp  = new String();
	   
	    try {
			while(result.next()) {
			
				if(result.getString(4).equals(user.getLogin())) {//on test si c'est un msg recu ou envoye pour ne pas les afficher de la mm facon, pour que cela soit reconnaissable
					
					tmp = result.getString(2) +" : " + result.getString(3) +"\n";
					this.mess.append(tmp);
					
				}else {
					
					tmp = "                                                                                                                                                " +result.getString(2) +" : " + result.getString(3) +"\n";
					this.mess.append(tmp);
					
				
				}
				
			    System.out.println(this.mess.getText());
			}
			panelHaut.add(this.mess);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    DB.deconnect();
	    //partie basse ou on tape notre text et envoie le message
	    JTextField newMessage = new JTextField("Enter new message");
	    panelBas.add(newMessage);
	    JButton send = new JButton("send message");
	    panelBas.add(send);
	    JButton back = new JButton("Back to Main Frame");
	    panelBas.add(back);
	    back.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				//on ferme la page de conversation
	    		frame.dispose();
			}
		});
	    
	    send.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
	    		Message newMess = new Message(newMessage.getText(), user, conv.getUser2());
				
	    		
	    		try {
	    			//on envoie le message via tcp via le socket link
				ObjectOutputStream oos=new ObjectOutputStream(link.getOutputStream());
				oos.writeObject(newMess);
				oos.flush();
				
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
	    		//ecriture du msg envoye dans l'interface graphique de l'envoyeur
	    		String tmp2 = new String();
	    		tmp2 = newMess.getDateEnvoie() +" : " + newMess.getData()+"\n";
				mess.append(tmp2);
				
				//panelHaut.add(mess);
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
