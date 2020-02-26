package appli;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import bri.ServeurBRi;
import bri.ServiceRegistry;

public class BRiLaunch {
	private final static int PORT_AMA = 3000;
	private final static int PORT_PROG = 2000;
	public static void main(String[] args) throws MalformedURLException {
		// URLClassLoader sur ftp
		
		URL[] tabURL = {new URL("ftp://localhost:2121/tp4/classes/"), new URL("ftp://localhost:2121/tp4/lib/")};
		URLClassLoader urlcl = new URLClassLoader(tabURL);
		
		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
		System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		
		new Thread(new ServeurBRi(PORT_AMA)).start();
		new Thread(new ServeurBRi(PORT_PROG)).start();
		
		/*
		while (true){
				try {
					String classeName = clavier.next();
					Class<?> newService = urlcl.loadClass(classeName);
					ServiceRegistry.addService(newService);
					System.out.println("Service '"+classeName+"' a été ajouté avec succès");
				} catch (Exception e) {
					System.out.println(e);
				}
			}*/		
	}
	
}
