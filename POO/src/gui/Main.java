package gui;

import clientLogin.User;

import java.awt.event.ActionListener;

import javax.swing.*;


public class Main{
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				//on a juste � appeler notre page de login tout le reste de l'application se fera depuis la page pr�c�dente
				Login pageLogin = new Login();				
			}
		});
	}
}
