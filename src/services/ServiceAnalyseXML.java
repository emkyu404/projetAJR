package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import bri.Service;

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
			
			out.println("Il faut vous connecter � votre mail pour recevoir le rapport");
			out.println("Seul gmail fonctionne");
			out.println("Pensez � activer l'option qui permet de recevoir des mails d'application moins s�curis�es");
			out.println("Quel est votre mail ?");
		
			String mail = in.readLine();
			
			out.println("Quel est votre mot de passe gmail ?");
			
			String mdp = in.readLine();
			
			out.println("Indiquer le nom avec extension du fichier xml � analyser");
			
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
	
	//Le main est l� pour tester sans passer par tout le processus d'ajout de service etc
	/*public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		validerXML("test.xml");
		//Messagerie mail = new Messagerie("anthonyjoaquim40@gmail.com","Anthonio123");
		//mail.envoyerMail("salut");
	}
	
	public static void validerXML(String fichierXML) throws SAXException, IOException, ParserConfigurationException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		//PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
		//out.println("Tapez un texte � inverser");
		Scanner clavier = new Scanner(System.in);
		System.out.println("Il faut vous connecter � votre mail pour recevoir le rapport");
		System.out.println("Seul gmail fonctionne");
		System.out.println("Pensez � activer l'option qui permet de recevoir des mails d'application moins s�curis�es");
		System.out.println("Quel est votre mail ?");
		
		String mail = clavier.nextLine();
		
		System.out.println("Quel est votre mdp gmail ?");
		
		String mdp = clavier.nextLine();
		Messagerie mailUser = new Messagerie(mail,mdp);
		
		builder.setErrorHandler(new SimpleErrorHandler(mailUser));
		
		Document document = builder.parse(new InputSource("ftp://localhost:2121/fichierXML/test.xml"));
	}*/
}
