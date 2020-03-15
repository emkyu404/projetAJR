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

/**
 * @author Bui Minh-Qu�n & Anthony Reino 
 * Service d'arr�t d'un service amateur, utilis� par les programmeurs uniquement
 */
public class ServiceArr�t implements Service {
	
	private Socket client;
	
	public ServiceArr�t(Socket sock, String log) {
		this.client = sock;
	}
	
	@Override
	public void run() {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
		PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
		out.println(ServiceRegistry.toStringue()+"##Indiquez le num�ro du service � arr�ter  (0 pour annuler)");
			try {
				int numService = Integer.parseInt(in.readLine());
				if(numService == 0) {
					out.println("Annulation du service d'arr�t. Retour � la s�lection de service ##*************************************************************************##");
				} else {
					Class<?> serviceToStop = ServiceRegistry.getServiceClass(numService);
					ServiceRegistry.stopService(serviceToStop);;
					String classeName = serviceToStop.getSimpleName();
					out.println("Service '"+classeName+"' a �t� arr�t� avec succ�s ##*************************************************************************##");
				}
			} catch (Exception e) {
				out.println("Erreur : Le service indiqu� n'existe pas. V�rifiez que le nom ne comporte aucune erreur.");
			}
		} catch (IOException e) {
		//Fin du service
		}
	}

}
