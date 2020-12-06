package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import clientClavardage.Conversation;
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
	    
	    JPanel panel = new JPanel(new GridLayout(2, 2));
	    panel.add(logOutButton);
	    
	    
	    
	    
	    JButton actualiseListConv = new JButton("Refresh conversation list");
	    panel.add(actualiseListConv);
	    actualiseListConv.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
				
	    		//ici on fait une requete dans la bdd pour remplacer la valeur de la liste par les champ de la bdd correspond aux conv qui contiennent l user logIn
			}
		});
	    
	    
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
	    		//frame.dispose();
			}
		});
	    
	    
	    
	    /*
	     * Récupération d'une liste de conversation dans la base de données en enfonction d'un itlisateur, celui qui s'est LogIn 
	     * on va ffaire ca plus tard je vais donc creer une liste moi init manuellement pour tester mon affichage
	     * 
	     * */
	    
	    /* ici je creer les variables conv pour les test*/
	    
	    User testDest = new User("le destinataire de test", 3, user.getIp(), 100);
	    Conversation convTest1 = new Conversation(user, testDest, 1);
	    Conversation convTest2 = new Conversation(user, testDest, 2);
	    
	    JList listConv = new JList();
	    
	    listConv.setModel(new AbstractListModel() {

            String[] strings = {convTest1.toString(), convTest2.toString()};

            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public Object getElementAt(int i) {
                return strings[i];
            }
        });
	    
	    
	    listConv.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent evt) {
                new ConversationPage(user);
                //frame.dispose();
            }
        });
	    
	    panel.add(listConv);
	    
	    contentPane.add(panel);
	    
	    frame.setVisible(true);
	       
	}
}
