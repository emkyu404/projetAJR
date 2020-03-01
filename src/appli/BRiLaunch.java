package appli;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import bri.ServeurBRi;
import bri.ServiceRegistry;

public class BRiLaunch {
	
	private final static int PORT_AMA = 3000;
	private final static int PORT_PROG = 2000;
	private final static int PORT_CONNEXION = 4000;
	
	public static void main(String[] args) throws MalformedURLException {
		// URLClassLoader sur ftp
		
		URL[] tabURL = {new URL("ftp://localhost:2121/tp4/classes/"), new URL("ftp://localhost:2121/tp4/lib/")};
		URLClassLoader urlcl = new URLClassLoader(tabURL);
		
		
		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		System.out.println("Les programmeurs se connectent au serveur 2000 pour lancer une activité");
		
		new Thread(new ServeurBRi(PORT_CONNEXION)).start();
		new Thread(new ServeurBRi(PORT_AMA)).start();
		new Thread(new ServeurBRi(PORT_PROG)).start();
		
	}
	
}
