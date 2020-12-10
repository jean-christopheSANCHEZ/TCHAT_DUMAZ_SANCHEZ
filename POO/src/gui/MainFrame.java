package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
				

				/*DATABASE connection*/
				//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				
				
				String DBurl = "jdbc:mysql://localhost/conv_mess?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
				Connection con = DriverManager.getConnection(DBurl, "root", "");
				ResultSet result = null;
				String requete = "SELECT * FROM conversation WHERE User1 = '" + user.getLogin() +"' or User2 = '" + user.getLogin() +"'";
				Statement stmt = con.createStatement();
				result = stmt.executeQuery(requete);
				
				
				
				
				List<String> strings = new ArrayList<String>();
				
				String stringInfo = null;
				ResultSetMetaData rsmd = result.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				   while (result.next()) {
					   stringInfo = "";
					   for (int i = 1; i <= columnsNumber; i++) {
				           //if (i > 1) System.out.print(",  "); 
				           String columnValue = result.getString(i);
				           //System.out.print(rsmd.getColumnName(i) + " : " + columnValue);
				           stringInfo = stringInfo + rsmd.getColumnName(i) + " : " + columnValue + "   ";
				           
				           
				       }
					   strings.add(stringInfo);
				       System.out.println("");
				   } 
				   System.out.println(strings);
				   
				   JList listConv = new JList();
				   String[] tabConv = new String[strings.size()];
				   tabConv = strings.toArray(tabConv);
				   
				   listConv.setModel(new AbstractListModel() {

					    
			            @Override
			            public int getSize() {
			                return strings.size();
			            }

			            @Override
			            public Object getElementAt(int i) {
			                return strings.get(i);
			            }
			        });
				   
				   
				   
				   
				   listConv.addListSelectionListener(new ListSelectionListener() {

			            @Override
			            public void valueChanged(ListSelectionEvent evt) {
			            	new ConversationPage(user);
			            }
			        });
				   
				   
				   panel.add(listConv);
				   frame.repaint();
				   frame.setVisible(true);

				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
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
	    
	    
	    
	   
	    
	    /*JList listConv = new JList();
	    
	    listConv.setModel(new AbstractListModel() {

            String[] strings = { }
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
	    */
	    
	    
	    contentPane.add(panel);
	    
	    frame.setVisible(true);
	       
	}
}
