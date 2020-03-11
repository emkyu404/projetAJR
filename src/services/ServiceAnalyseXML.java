package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Validator;
import javax.validation.ValidatorFactory;

import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation;

import bri.Service;

public class ServiceAnalyseXML implements Service {
	
	private final Socket client;
	
	public ServiceAnalyseXML(Socket socket) {
		client = socket;
	}
	
	// EN CONSTRUCTION !!!!!!!!
	@Override
	public void run()  {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
		try {
		URL[] tabURL = {new URL("ftp://localhost:2121/tp4/lib/"),new URL("ftp://localhost:2121/tp4/fichierXML/")};
		
		URLClassLoader urlcl = new URLClassLoader(tabURL);
		
			try {
				BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
				PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
				
				//Etape 1 : Reception du fichier
				out.println("Indiquez le nom du fichier XML à analyser");
				try {
					String fileName = in.readLine();
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					//Class<?> newFile = urlcl.loadClass(fileName);
					//InputStream newFile = ClassLoader.getSystemResourceAsStream(fileName);
					
					//Ce print est un test
					//out.println(newFile.getName());
					
				} catch (Exception e) {
					out.println("Erreur : Le fichier indiqué n'existe pas. Vérifiez que le nom ne comporte aucune erreur.");
				}
				
				//Etape 2 : Analyse du fichier
				
				
				
				
				
				//Etape 3 : Envoi de l'analyse par mail via la classe Messagerie
				
				
				
				
				
				
			} catch (IOException e) {
			//Fin du service
			}
		} catch(Exception e) {
			}
		}

	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	public static String toStringue() {
		return "Inversion de texte";
	}
}
