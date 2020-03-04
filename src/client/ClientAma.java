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
 * Ce client se connecte � un serveur dont le protocole est 
 * menu-choix-question-r�ponse client-r�ponse service
 * il n'y a qu'un �change (pas de boucle)
 * la r�ponse est saisie au clavier en String
 * 
 * Le protocole d'�change est suffisant pour le tp4 avec ServiceInversion 
 * ainsi que tout service qui pose une question, traite la donn�e du client et envoie sa r�ponse 
 * mais est bien sur susceptible de (nombreuses) am�liorations
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
			
			//Bool�en qui passe � true si l'utilisateur a �t� trouv�
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
			
				System.out.println("Connect� au serveur " + s.getInetAddress() + ":"+ s.getPort());
				
				String line;
			// menu et choix du service
				line = sin.readLine();
				System.out.println(line.replaceAll("##", "\n"));
			// saisie/envoie du choix
				sout.println(clavier2.readLine());
				
			// r�ception/affichage de la question
				line = sin.readLine();
				System.out.println(line.replaceAll("##", "\n"));
			// saisie clavier/envoie au service de la r�ponse
				sout.println(clavier2.readLine());
			// r�ception/affichage de la r�ponse
				System.out.println(sin.readLine());
					
			}
			catch (IOException e) { System.err.println("Fin de la connexion"); }
			// Refermer dans tous les cas la socket
			try { if (s != null) s.close(); } 
			catch (IOException e2) { ; }
		}
}
