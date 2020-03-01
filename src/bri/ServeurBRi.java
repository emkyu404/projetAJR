package bri;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appli.BRiLaunch;


public class ServeurBRi implements Runnable {
	private ServerSocket listen_socket;
	private int currentPort;
	
	//clé : role de l'utilisateur, valeur : une liste qui contient son login et mot de passe
	private static Map<String, List<String>> users = new HashMap<>();
	
	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurBRi(int port) {
		try {
			listen_socket = new ServerSocket(port);
			currentPort = port;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	//Initialisaton des données des utilisateurs
	public static void init() {
		List<String> loginAma = new ArrayList<>();
		loginAma.add("logAma");
		loginAma.add("pwdAma");
		
		List<String> loginProg = new ArrayList<>();
		loginProg.add("logProg");
		loginProg.add("pwdProg");
		
		users.put("amateur",loginAma);
		users.put("programmeur", loginProg);
			
	}

	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un ServiceInversion, 
	// qui va la traiter.
	public void run() {
		try {
			while(true) {
				ServeurBRi.init();
				switch(currentPort) {
					case 3000 : new ServiceBRiAma(listen_socket.accept()).start(); break;
					case 2000 : new ServiceBRiProg(listen_socket.accept()).start(); break;
				}
			}
		}
		catch (IOException e) { 
			try {this.listen_socket.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'écoute :"+e);
		}
	}

	 // restituer les ressources --> finalize
	protected void finalize() throws Throwable {
		try {this.listen_socket.close();} catch (IOException e1) {}
	}

	// lancement du serveur
	public void lancer() {
		(new Thread(this)).start();		
	}
	
	public static Map<String, List<String>> getUserLogs(){
		return users;
	}
}
