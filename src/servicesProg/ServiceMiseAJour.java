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

public class ServiceMiseAJour implements Service {
	
	private Socket client;
	private String login;
	
	public ServiceMiseAJour(Socket sock, String log) {
		this.client = sock;
		this.login = log;
	}
	
	@Override
	public void run()  {
	
		try {
		URL[] tabURL = {new URL(ServiceRegistry.getServerFTPURLClass()), new URL(ServiceRegistry.getServerFTPURLClass())};
		URLClassLoader urlcl = new URLClassLoader(tabURL);
		
			try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println(ServiceRegistry.toStringueStopped()+"##Indiquez le nom du service � mettre � jour (0 pour annuler)");
				try {
					String classeName = in.readLine();
					if(classeName.equals("0")) {
						out.println("Annulation du service de mise � jour. Retour � la s�lection de service ##*************************************************************************##");
					} else {
						Class<?> updatedService = urlcl.loadClass(login + "." + classeName);
						classeName = updatedService.getSimpleName();
						ServiceRegistry.updateService(updatedService, login);
						out.println("Service '"+classeName+"' a �t� mis � jour avec succ�s##*************************************************************************##");
					}
				} catch (Exception e) {
					out.println("Erreur : Le service indiqu� n'existe pas. V�rifiez que le nom ne comporte aucune erreur.");
				}
			} catch (IOException e) {
				PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
				out.println("Erreur : " + e.getMessage());
			}
		} catch(Exception e) {
		
	}
		
	}
}

