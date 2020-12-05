package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import clientClavardage.Conversation;
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
	       contentPane.setLayout(new SpringLayout());

	       //Add the buttons.
	       JButton newConvButton = new JButton("Create new conversation");
	       
	       
	       userDest_label = new JLabel();
	       userDest_label.setText("Destinataire :");
	       userDest_text = new JTextField();
	       
	       portDest_label = new JLabel();
	       portDest_label.setText("Port number :");
	       portDest_text = new JTextField();
	       
	       id_label = new JLabel();
	       id_label.setText("Unique Id conv :");
	       id_text = new JTextField();
	       
	       panel = new JPanel(new GridLayout(4, 1));
	       
	       panel.add(userDest_label);
	       panel.add(userDest_text);
	       panel.add(portDest_label);
	       panel.add(portDest_text);
	       panel.add(id_label);
	       panel.add(id_text);
	       panel.add(newConvButton);
	       add(panel, BorderLayout.CENTER);
	       
	       
	       contentPane.add(panel);
	       
	       
	       newConvButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					// envoie dans la bdd la nouvelle conv evec les infos
					try {
						User destinataire = new User(userDest_text.getText(), 1, InetAddress.getLocalHost(), Integer.parseInt(portDest_text.getText()));
						Conversation newConv = new Conversation(user, destinataire, Integer.parseInt(id_text.getText()));
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
		   frame.setSize(300, 600);
		   frame.setTitle("LogIn");
	       frame.setVisible(true);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
