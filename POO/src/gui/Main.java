package gui;

import clientLogin.User;

import java.awt.event.ActionListener;

import javax.swing.*;


public class Main{
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				//on a juste à appeler notre page de login tout le reste de l'application se fera depuis la page précédente
				Login pageLogin = new Login();				
			}
		});
	}
}
