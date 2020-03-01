package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import appli.BRiLaunch;
import bri.ServeurBRi;

public class ConnexionClient {
	private final static int PORT_SERVICE = 4000;
	private final static String HOST = "localhost"; 
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Socket s = null;		
		try {
			s = new Socket(HOST, PORT_SERVICE);
			
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));			
		
			System.out.println("Connecté au serveur " + s.getInetAddress() + ":"+ s.getPort());
			
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
			
			//On recherche si les logs de l'utilisateur existent puis on lance le bon client en fonction de rôle
			for (Map.Entry<String, List<String>> entry : ServeurBRi.getUserLogs().entrySet()) {
	            String key = entry.getKey();
	            List<String> values = entry.getValue();
	            
	            if(values.get(0).equals(login) && values.get(1).equals(password)) {
	            	if(key.equals("amateur")) {
	            		//Bonne méthode ?
	            		Class<?> clientAmaClass = Class.forName("client.ClientAma");
	            		ClientAma cliAma = (ClientAma) clientAmaClass.newInstance();
	            		cliAma.main(args);
	            	}
	            	else if(key.equals("programmeur")) {
	            		Class<?> clientProgClass = Class.forName("client.ClientProg");
	            		ClientProg cliAma = (ClientProg) clientProgClass.newInstance();
	            		cliAma.main(args);
	            	}
	            }
	        }	
			
			if(!exists) {
				System.err.println("Erreur : Login/Mot de passe incorrect");
			}
			
		}
		catch (IOException e) { System.err.println("Fin de la connexion"); }
		// Refermer dans tous les cas la socket
		try { if (s != null) s.close(); } 
		catch (IOException e2) { ; }	
	}
}
