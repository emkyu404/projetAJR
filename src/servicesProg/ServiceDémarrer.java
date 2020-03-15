package servicesProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bri.Service;
import bri.ServiceRegistry;

/**
 * @author Bui Minh-Qu�n & Anthony Reino 
 * Service de d�marrage d'un service amateur, utilis� par les programmeurs uniquement
 */
public class ServiceD�marrer implements Service {
	
	private Socket client;
	
	public ServiceD�marrer(Socket sock, String log) {
		this.client = sock;
	}
	
	
	@Override
	public void run() {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
		PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
		out.println(ServiceRegistry.toStringueStopped()+"##Indiquez le num�ro du service � d�marrer (0 pour annuler)");
			try {
				int numService = Integer.parseInt(in.readLine());
				if(numService == 0) {
					out.println("Annulation du service de d�marrage. Retour � la s�lection de service ##*************************************************************************##");
				}else {
					Class<?> serviceToStart = ServiceRegistry.getServiceStoppedClass(numService);
					ServiceRegistry.startService(serviceToStart);;
					String classeName = serviceToStart.getSimpleName();
					out.println("Service '"+classeName+"' a �t� d�marrer avec succ�s ##*************************************************************************##");
				}
			} catch (Exception e) {
				out.println("Erreur : Le service indiqu� n'existe pas. V�rifiez que le nom ne comporte aucune erreur.");
			}
		} catch (IOException e) {
		//Fin du service
		}

	}

}
