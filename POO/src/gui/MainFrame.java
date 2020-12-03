package gui;

import javax.swing.JFrame;

import clientLogin.User;

public class MainFrame {
	private JFrame frame = new JFrame("Second");
	
	public MainFrame(User user) {
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920,1080);
		frame.setTitle(user.getLogin()+" connected on port : " +user.getNumPort());
		frame.setVisible(true);
	}
}
