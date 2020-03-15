package services;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SimpleErrorHandler implements ErrorHandler {
	
	private Messagerie mail;
	
	public SimpleErrorHandler(Messagerie m) {
		this.mail = m;
	}
	
	@Override
	public void error(SAXParseException e) throws SAXException {
		//System.out.println(e.getMessage());
		mail.envoyerMail(e.getMessage());
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		//System.out.println(e.getMessage());
		mail.envoyerMail(e.getMessage());
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		//System.out.println(e.getMessage());
		mail.envoyerMail(e.getMessage());
	}

}
