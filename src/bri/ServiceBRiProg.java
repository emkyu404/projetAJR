package bri;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;


class ServiceBRiProg implements Runnable {
	
	private Socket client;
	private String log;
	
	ServiceBRiProg(Socket socket) {
		client = socket;
	}

	public void run() {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			log = in.readLine();	
			boolean active = true;
			while(active) {
				out.println("##" +ServiceRegistry.toStringueProg()+"##------------------------------------------------------------------------##"+log+", Tapez le numéro de service désiré (0 pour se déconnecter) :");
				try {
					int choix = Integer.parseInt(in.readLine());
					if(choix == 0) {
						active = false;
						out.println("Déconnexion de l'utilisateur " + log + ". A bientôt !");
					} else {
					
						// instancier le service numéro "choix" en lui passant la socket "client"
						// invoquer run() pour cette instance ou la lancer dans un thread à part 
						try {
							((Service) ServiceRegistry.getServicesClassesProg(choix).getConstructor(Socket.class, String.class).newInstance(client, log)).run();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							
						}
					}
				}catch(Exception e) {
					
				}	
		}
	} catch (IOException e) {
			//Fin du service
	}

		try {client.close();} catch (IOException e2) {}
	
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	// lancement du service
	public void start() {
		(new Thread(this)).start();		
	}

}
