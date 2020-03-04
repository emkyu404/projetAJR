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
	private String log;
	
	public ServiceAjout(Socket socket, String progLogin) {
		client = socket;
		log = progLogin;
	}
	
	@Override
	public void run()  {
	
		try {
		URL[] tabURL = {new URL("ftp://localhost:2121/tp4/classes/"), new URL("ftp://localhost:2121/tp4/lib/")};
		URLClassLoader urlcl = new URLClassLoader(tabURL);
		
			try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println(ServiceRegistry.toStringue()+"##Indiquez le nom du service à ajouter");
				try {
					String classeName = in.readLine();
					Class<?> newService = urlcl.loadClass(classeName);
					ServiceRegistry.addService(newService, log);
					out.println("Service '"+classeName+"' a été ajouté avec succès");
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
