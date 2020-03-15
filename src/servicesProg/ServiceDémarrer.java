package servicesProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bri.Service;
import bri.ServiceRegistry;

/**
 * @author Bui Minh-Quân & Anthony Reino 
 * Service de démarrage d'un service amateur, utilisé par les programmeurs uniquement
 */
public class ServiceDémarrer implements Service {
	
	private Socket client;
	
	public ServiceDémarrer(Socket sock, String log) {
		this.client = sock;
	}
	
	
	@Override
	public void run() {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
		PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
		out.println(ServiceRegistry.toStringueStopped()+"##Indiquez le numéro du service à démarrer (0 pour annuler)");
			try {
				int numService = Integer.parseInt(in.readLine());
				if(numService == 0) {
					out.println("Annulation du service de démarrage. Retour à la sélection de service ##*************************************************************************##");
				}else {
					Class<?> serviceToStart = ServiceRegistry.getServiceStoppedClass(numService);
					ServiceRegistry.startService(serviceToStart);;
					String classeName = serviceToStart.getSimpleName();
					out.println("Service '"+classeName+"' a été démarrer avec succès ##*************************************************************************##");
				}
			} catch (Exception e) {
				out.println("Erreur : Le service indiqué n'existe pas. Vérifiez que le nom ne comporte aucune erreur.");
			}
		} catch (IOException e) {
		//Fin du service
		}

	}

}
