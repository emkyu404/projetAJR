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
 * @author Bui Minh-Quân & Anthony Reino 
 * Service d'arrêt d'un service amateur, utilisé par les programmeurs uniquement
 */
public class ServiceArrêt implements Service {
	
	private Socket client;
	
	public ServiceArrêt(Socket sock, String log) {
		this.client = sock;
	}
	
	@Override
	public void run() {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
		PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
		out.println(ServiceRegistry.toStringue()+"##Indiquez le numéro du service à arrêter  (0 pour annuler)");
			try {
				int numService = Integer.parseInt(in.readLine());
				if(numService == 0) {
					out.println("Annulation du service d'arrêt. Retour à la sélection de service ##*************************************************************************##");
				} else {
					Class<?> serviceToStop = ServiceRegistry.getServiceClass(numService);
					ServiceRegistry.stopService(serviceToStop);;
					String classeName = serviceToStop.getSimpleName();
					out.println("Service '"+classeName+"' a été arrêté avec succès ##*************************************************************************##");
				}
			} catch (Exception e) {
				out.println("Erreur : Le service indiqué n'existe pas. Vérifiez que le nom ne comporte aucune erreur.");
			}
		} catch (IOException e) {
		//Fin du service
		}
	}

}
