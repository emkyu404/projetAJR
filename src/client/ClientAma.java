package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import bri.ServeurBRi;

/*
 * Ce client se connecte à un serveur dont le protocole est 
 * menu-choix-question-réponse client-réponse service
 * il n'y a qu'un échange (pas de boucle)
 * la réponse est saisie au clavier en String
 * 
 * Le protocole d'échange est suffisant pour le tp4 avec ServiceInversion 
 * ainsi que tout service qui pose une question, traite la donnée du client et envoie sa réponse 
 * mais est bien sur susceptible de (nombreuses) améliorations
 */
class ClientAma {
		private final static int PORT_SERVICE = 3000;
		private final static String HOST = "localhost"; 
	
		public static void main(String[] args) throws IOException {
			
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Bienvenue sur le client amateur");
			System.out.println("Entrez votre login");
			String login;
			login = clavier.readLine();
			
			System.out.println("Entrez votre mot de passe");
			String password;
			password = clavier.readLine();
			
			//A modifier ??
			ServeurBRi.init();
			
			//Booléen qui passe à true si l'utilisateur a été trouvé
			boolean exists = false;
			
			//On recherche si les logs de l'utilisateur existent
			for (Map.Entry<String, List<String>> entry : ServeurBRi.getUserLogs().entrySet()) {
	            String key = entry.getKey();
	            List<String> values = entry.getValue();
	            
	            if(values.get(0).equals(login) && values.get(1).equals(password)) {
	            	if(key.equals("amateur")) {
	            		exists = true;
	            		AmaLauncher();
	            	}
	            }
	        }	
			
			if(!exists) {
				System.err.println("Erreur : Login/Mot de passe incorrect");
			}		
		}
		
		public static void AmaLauncher() {
			Socket s = null;		
			try {
				s = new Socket(HOST, PORT_SERVICE);
				
				BufferedReader clavier2 = new BufferedReader(new InputStreamReader(System.in));
				BufferedReader sin = new BufferedReader (new InputStreamReader(s.getInputStream ( )));
				PrintWriter sout = new PrintWriter (s.getOutputStream ( ), true);			
			
				System.out.println("Connecté au serveur " + s.getInetAddress() + ":"+ s.getPort());
				
				String line;
			// menu et choix du service
				line = sin.readLine();
				System.out.println(line.replaceAll("##", "\n"));
			// saisie/envoie du choix
				sout.println(clavier2.readLine());
				
			// réception/affichage de la question
				line = sin.readLine();
				System.out.println(line.replaceAll("##", "\n"));
			// saisie clavier/envoie au service de la réponse
				sout.println(clavier2.readLine());
			// réception/affichage de la réponse
				System.out.println(sin.readLine());
					
			}
			catch (IOException e) { System.err.println("Fin de la connexion"); }
			// Refermer dans tous les cas la socket
			try { if (s != null) s.close(); } 
			catch (IOException e2) { ; }
		}
}
