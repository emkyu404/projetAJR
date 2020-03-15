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

public class ServiceDésinstaller implements Service {
	private Socket client;
	
	public ServiceDésinstaller(Socket socket, String log) {
		client = socket;
	}
	
	@Override
	public void run()  {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
		PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
		out.println(ServiceRegistry.toStringueStopped()+"##Indiquez le numéro du service à désinstaller  (0 pour annuler)");
			try {
				int numService = Integer.parseInt(in.readLine());
				if(numService == 0) {
					out.println("Annulation du service de désintallation. Retour à la sélection de service ##*************************************************************************##");
				}else {
					Class<?> newService = ServiceRegistry.getServiceStoppedClass(numService);
					String classeName = newService.getSimpleName();					
					ServiceRegistry.removeService(newService);
					out.println("Service '"+classeName+"' a été désinstaller avec succès ##*************************************************************************##");
				}
			} catch (Exception e) {
				out.println("Erreur : Le service indiqué n'existe pas. Vérifiez que le numero indiqué ne comporte aucune erreur.");
			}
		} catch (IOException e) {
		//Fin du service
		}
		
	}

}
