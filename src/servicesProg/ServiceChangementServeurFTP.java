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
 * Service de changement d'adresse du serveurFTP, utilisé par les programmeurs uniquement
 */
public class ServiceChangementServeurFTP implements Service {
	private Socket client;
	
	public ServiceChangementServeurFTP(Socket socket, String log) {
		client = socket;
	}

	@Override
	public void run() {
		try {
			try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			out.println("##Indiquez la nouvelle adresse FTP (ex : 'ftp://localhost:2121/projet') - (0 pour annuler)");
				try {
					String newAddress = in.readLine();
					if(newAddress.equals("0")) {
						out.println("Annulation du service de changement de serveur FTP. Retour à la sélection de service ##*************************************************************************##");
					}else {
						String newAddressClass = newAddress + "/classes/";
						String newAddressLib = newAddress +"/lib/";
						ServiceRegistry.changeFTPServer(newAddressClass, newAddressLib);
						out.println("L'adresse du serveur FTP a bien été modifié. Nouvelle adresse : "+newAddress+" ##*************************************************************************##");
					}
				} catch (Exception e) {
					out.println("Erreur : " + e.getMessage());
				}
			} catch (IOException e) {
			//Fin du service
			}
		} catch(Exception e) {
	
		}

	}
}