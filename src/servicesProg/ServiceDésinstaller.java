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
	
	public ServiceDésinstaller(Socket socket) {
		client = socket;
	}
	
	@Override
	public void run()  {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
		PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
		out.println(ServiceRegistry.toStringueStopped()+"##Indiquez le numéro du service à désinstaller");
			try {
				int serviceNumber = Integer.parseInt(in.readLine());
				Class<?> newService = ServiceRegistry.getServiceStoppedClass(serviceNumber);
				String classeName = newService.getSimpleName();					
				ServiceRegistry.removeService(newService);
				out.println("Service '"+classeName+"' a été désinstaller avec succès");
			} catch (Exception e) {
				out.println("Erreur : Le service indiqué n'existe pas. Vérifiez que le numero indiqué ne comporte aucune erreur.");
			}
		} catch (IOException e) {
		//Fin du service
		}

	try {client.close();} catch (IOException e2) {}
		
	}

}
