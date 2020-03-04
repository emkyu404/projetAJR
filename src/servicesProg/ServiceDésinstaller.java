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

public class ServiceD�sinstaller implements Service {
	private Socket client;
	
	public ServiceD�sinstaller(Socket socket) {
		client = socket;
	}
	
	@Override
	public void run()  {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
		PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
		out.println(ServiceRegistry.toStringueStopped()+"##Indiquez le num�ro du service � d�sinstaller");
			try {
				int serviceNumber = Integer.parseInt(in.readLine());
				Class<?> newService = ServiceRegistry.getServiceStoppedClass(serviceNumber);
				String classeName = newService.getSimpleName();					
				ServiceRegistry.removeService(newService);
				out.println("Service '"+classeName+"' a �t� d�sinstaller avec succ�s");
			} catch (Exception e) {
				out.println("Erreur : Le service indiqu� n'existe pas. V�rifiez que le numero indiqu� ne comporte aucune erreur.");
			}
		} catch (IOException e) {
		//Fin du service
		}

	try {client.close();} catch (IOException e2) {}
		
	}

}
