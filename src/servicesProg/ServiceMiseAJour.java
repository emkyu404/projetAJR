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
	
	public ServiceMiseAJour(Socket sock) {
		this.client = sock;
	}
	
	@Override
	public void run()  {
	
		try {
		URL[] tabURL = {new URL("ftp://localhost:2121/tp4/classes/"), new URL("ftp://localhost:2121/tp4/lib/")};
		URLClassLoader urlcl = new URLClassLoader(tabURL);
		
			try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println(ServiceRegistry.toStringueStopped()+"##Indiquez le nom du service à mettre à jour");
				try {
					String classeName = in.readLine();
					Class<?> updatedService = urlcl.loadClass(classeName);
					classeName = updatedService.getSimpleName();
					ServiceRegistry.updateService(updatedService);
					out.println("Service '"+classeName+"' a été mis à jour avec succès");
				} catch (Exception e) {
					out.println("Erreur : Le service indiqué n'existe pas. Vérifiez que le nom ne comporte aucune erreur.");
				}
			} catch (IOException e) {
			//Fin du service
			}
		} catch(Exception e) {
		
	}

	try {client.close();} catch (IOException e2) {}
		
	}
}

