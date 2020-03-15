package logProg;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Messagerie {
	
	private String mailDest;
	private String mdp;
	
	
	public Messagerie(String m, String mdp) {
		this.mailDest = m;
		this.mdp = mdp;
	}
	
	//Fonctionne seulement avec gmail
	public void envoyerMail(String contenuMail) {
		
		//On crée la session
		Properties proprietes = new Properties();
		
		proprietes.put("mail.smtp.auth", "true");
		proprietes.put("mail.smtp.starttls.enable","true");
		proprietes.put("mail.smtp.host","smtp.gmail.com");
		proprietes.put("mail.smtp.port","587");
		
		Session session = Session.getInstance(proprietes,new javax.mail.Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailDest, mdp);
			}
		});
		
		// On crée le message à envoyer
		try {
		
		Message message = new MimeMessage(session);
		
		message.setFrom(new InternetAddress("serveurBri@gmail.com"));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mailDest));
		
		message.setSubject("Compte rendu analyse du fichier XML");
		
		// /!\ Mettre dedans le contenu de l'analyse du fichier /!\
		message.setText(contenuMail);
		
		
		//On envoie le message
		Transport.send(message);
		//System.out.println("Le message a bien été envoyé");
		} 
		catch (MessagingException e) {
			throw new RuntimeException(e);
		} 
	}
}
