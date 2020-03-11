package services;

import java.io.IOException;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import bri.Service;

public class AnalyseXML implements Service{
	
	private final Socket client;
	
	public AnalyseXML(Socket socket) {
		client = socket;
	}
	
	@Override
	public void run() {
		
	}
	
	//Le main est là pour tester sans passer par tout le processus d'ajout de service etc
	/*public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		validerXML("test.xml");
	}*/
	
	public static void validerXML(String fichierXML) throws SAXException, IOException, ParserConfigurationException {
		 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		builder.setErrorHandler(new SimpleErrorHandler());
		
		Document document = builder.parse(new InputSource("test.xml"));
	}
}
