package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import clientClavardage.Conversation;
import clientClavardage.DatabaseConv_mess;
import clientLogin.DatabaseLogin;
import clientLogin.User;

public class NewConversationForm extends JFrame implements ActionListener{
	
	public User userCreant, userDest; 
	   
	JPanel panel;
	JLabel userDest_label, portDest_label, id_label;
	JTextField userDest_text, portDest_text, id_text;
	
	public NewConversationForm(User user) {
		//Create and set up the window.
	       JFrame frame = new JFrame("New conv");
	       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   
	       
	     //Set up the content pane.
	       Container contentPane = frame.getContentPane();
	       contentPane.setLayout(new GridLayout(1,2));

	       //Add the buttons.
	       JButton newConvButton = new JButton("Create new conversation");
	       
	       JButton back = new JButton("Back to Main Frame");
		    back.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent ae) {
					//save a conversation dans la bdd et ouvre la page de conversation
		    		//new MainFrame(user);
		    		frame.dispose();
				}
			});
	       
	       userDest_label = new JLabel();
	       userDest_label.setText("Destinataire :");
	       userDest_text = new JTextField();
	       
	       portDest_label = new JLabel();
	       portDest_label.setText("Port number :");
	       portDest_text = new JTextField();

	       panel = new JPanel(new GridLayout(5, 1));
	       JPanel panelUsers = new JPanel(new GridLayout(10,2));
	       JLabel info= new JLabel("Utilisateurs connectés :");
	       panelUsers.add(info);
	       
	       DatabaseLogin DB = new DatabaseLogin(user.getLogin(), user.getNumPort());
		   DB.selectUsers();
		   ResultSet result = DB.getResult();
		   String tmp = new String();
		   JTextArea userCo = new JTextArea();
		    try {
				while(result.next()) {
					tmp = tmp + result.getString(2) + " connecté sur le port " + result.getString(3) +"\n";
					
					
				}
				userCo.setText(tmp);
				panelUsers.add(userCo);
		    }catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	       
	       panel.add(userDest_label);
	       panel.add(userDest_text);
	       panel.add(portDest_label);
	       panel.add(portDest_text);
	       panel.add(newConvButton);
	       panel.add(back);
	       add(panel, BorderLayout.CENTER);
	       
	       
	       contentPane.add(panel);
	       contentPane.add(panelUsers);
	       
	       
	       newConvButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					// envoie dans la bdd la nouvelle conv avec les infos
					try {
						
						User destinataire = new User(userDest_text.getText(), 1, InetAddress.getLocalHost(), Integer.parseInt(portDest_text.getText()));
						DatabaseConv_mess DB = new DatabaseConv_mess(user.getLogin(), user.getNumPort(), destinataire.getLogin(), destinataire.getNumPort());
						DB.addConversation();
						DB.deconnect();
						frame.dispose();
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
	       
		   frame.pack();
		   frame.setSize(900, 600);
		   frame.setTitle("Create new converstion");
	       frame.setVisible(true);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
