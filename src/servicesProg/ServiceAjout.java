package servicesProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import bri.Service;
import bri.ServiceRegistry;

public class ServiceAjout implements Service {

	private Socket client;
	private String login;
	
	public ServiceAjout(Socket socket, String log) {
		client = socket;
		login = log;
	}
	
	@Override
	public void run()  {
	
		try {
		URL[] tabURL = {new URL(ServiceRegistry.getServerFTPURLClass()), new URL(ServiceRegistry.getServerFTPURLClass())};
		//URL[] tabURL = {new URL("ftp://localhost:2121/tp4/classes/"), new URL("ftp://localhost:2121/tp4/lib/")};
		URLClassLoader urlcl = new URLClassLoader(tabURL);
		
			try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println(ServiceRegistry.toStringue()+"##Indiquez le nom du service � ajouter  (0 pour annuler)");
				try {
					String classeName = in.readLine();
					if(classeName.equals("0")) {
						out.println("Annulation du d�marrage. Retour � la s�lection de service ##*************************************************************************##");
					}else {
						Class<?> newService = urlcl.loadClass(login + "." + classeName);
						ServiceRegistry.addService(newService, login);
						out.println("Service '"+classeName+"' a �t� ajout� avec succ�s ##*************************************************************************##");
					}
				} catch (Exception e) {
					out.println("Erreur : " + e.getMessage());
				}
			} catch (IOException e) {
				PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
				out.println("Erreur : " + e.getMessage());
			}
		} catch(Exception e) {
		
	}
		
	}

}
