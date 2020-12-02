package gui;

import clientLogin.User;

import java.awt.event.ActionListener;

import javax.swing.*;


public class Main{
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				//On crée une nouvelle instance de notre JDialog
				JDialog dialog = new JDialog();
				dialog.setSize(1920, 1080);//On lui donne une taille
				dialog.setTitle("Première fenêtre"); //On lui donne un titre
				dialog.setVisible(false);//On la rend visible
				//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix
				
				Login pageLogin = new Login();
				
				
				dialog.setVisible(true);//On la rend visible
				User utilisateur = pageLogin.getNewUser();
				JLabel loginUtilisateur;
				JPanel panelPrincipal = new JPanel();
				loginUtilisateur = new JLabel(utilisateur.getLogin());				
				panelPrincipal.add(loginUtilisateur);
				panelPrincipal.setVisible(true);
			}
		});
	}
	
	
	
}

