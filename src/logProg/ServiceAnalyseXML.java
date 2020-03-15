package logProg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import bri.Service;
import bri.ServiceRegistry;

public class ServiceAnalyseXML implements Service{
	
	private final Socket client;
	
	public ServiceAnalyseXML(Socket socket) {
		client = socket;
	}
	
	@Override
	public void run() {
		
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			out.println("Il faut vous connecter à votre mail pour recevoir le rapport");
			out.println("Seul gmail fonctionne");
			out.println("Pensez à activer l'option qui permet de recevoir des mails d'application moins sécurisées");
			out.println("Quel est votre mail ?");
		
			String mail = in.readLine();
			
			out.println("Quel est votre mot de passe gmail ?");
			
			String mdp = in.readLine();
			
			out.println("Indiquer le nom avec extension du fichier xml à analyser");
			
			String nomXML = in.readLine();
			
			Messagerie mailUser = new Messagerie(mail,mdp);
			
			builder.setErrorHandler(new SimpleErrorHandler(mailUser));
			//Document document = builder.parse(new InputSource("test.xml"));
			Document document = builder.parse(new InputSource("ftp://localhost:2121/tp4/fichierXML/" + nomXML));
			
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
	
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public static String toStringue() {
		return "Analyse de fichier XML";
	}
}
