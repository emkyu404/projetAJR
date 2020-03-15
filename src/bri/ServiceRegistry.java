package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import servicesProg.*;


/**
 * @author Bui Minh-Quân & Anthony Reino 
 * Classe écrite à partir du TP4 et qui regroupe toutes les manipulations liées au service, assui bien pour les utilisateurs dit "amateurs"
 * que pour les utilisateurs dit "prorammeur"
 */
public class ServiceRegistry {
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesAmaClassesStarted = new Vector<Class<?>>();
		servicesAmaClassesStopped = new Vector<Class<?>>();
		servicesProgClasses = new Vector<Class<?>>(); 
		
		// Nous avons configuré par défaut le dossier sur "tp4"
		serverFTPURLClass = "ftp://localhost:2121/tp4/classes/";
		serverFTPURLLib = "ftp://localhost:2121/tp4/lib/";
		try{
			initializeProgServices(); // ajoute les services programmeurs 
		}catch(Exception e) {
			
		}
	}
	
	private static String serverFTPURLClass; // URL du serveur FTP (pour classes)
	private static String serverFTPURLLib; // URL du serveur FTP (pour lib)
	private static List<Class<?>> servicesAmaClassesStarted; // Liste des classes de services amateurs déployés 
	private static List<Class<?>> servicesAmaClassesStopped; // Liste des classes de services amateurs installé en attente de déploiement
	private static List<Class<?>> servicesProgClasses; // Liste des classes de services programmeur
	
	/* permet de récupérer l'adresse du serveurFTP */
	public static String getServerFTPURLClass() {
		return serverFTPURLClass;
	}
	/* permet de récupererl 'adresse du serveur FTP */
	public static String getServerFTPURLLib() {
		return serverFTPURLLib;
	}
	
	
	/**
	 * Ajout d'une classe suivant les vérifications BRi 
	 * @param newService : Class du nouveau service
	 * @param loginProg : identifiant du programmeur qui appelle la fonction
	 * @throws AddServiceException
	 */
	public static void addService(Class<?> newService, String loginProg) throws AddServiceException {
	
		int modifier = newService.getModifiers();
		Class<?>[] interfaces = newService.getInterfaces();
		
		/* recherche si le service que l'on souhaite implémenté est dans un package au nom du programmeur connecté */
		if(!checkServPackage(loginProg, newService.getPackageName())) throw new AddServiceException("Le service que vous souhaitez implémenté ne vous appartient pas");
		
		
		/* recherche si le service implémente la classe ServiceBRI */
		if(!checkInterface(interfaces)) throw new AddServiceException("Le service n'implemente pas la classe ServiceBRi");
		
		/* recherche si le service est une classe publique */
		if(!Modifier.isPublic(modifier)) throw new AddServiceException("Le service n'est pas une classe publique");
		
		/* recherche si le service est une classe abstraite */
		if(Modifier.isAbstract(modifier)) throw new AddServiceException("Le service est une classe abstraite");
		
		/* recherche si le service a un constructeur publique */
		if(!hasPublicConstructor(newService)) throw new AddServiceException("Le service n'a pas de constructeur publique avec en paramètre un Socket");
		
		/* recherche si le service a un attribut de type Socket */
		if(!hasSocketAttr(newService)) throw new AddServiceException("Le service n'a pas de Socket en attribut");
		
		/* recherche si le service a une méthode toString */
		if(!hasToString(newService)) throw new AddServiceException("Le service n'a pas de méthode 'public static String oStringue()'");
		
		/* recherche dans la liste des classes de services arrêtés si le service existe déjà */
		for(Class<?> service : servicesAmaClassesStopped) {
			if(service.getSimpleName().equals(newService.getSimpleName())){
				throw new AddServiceException("Le service que vous cherchez à implémenter existe déjà. Utiliser la fonctionnalité de mise à jour.");
			}
		}
		
		/* recherche dans la liste des classes de services déployés si le service existe déjà */
		for(Class<?> service : servicesAmaClassesStarted) {
			if(service.getSimpleName().equals(newService.getSimpleName())){
				throw new AddServiceException("Le service que vous cherchez à implémenter existe déjà. Utiliser la fonctionnalité de mise à jour.");
			}
		}
		
		// le service est ajouté par défaut dans la liste des services déployé
		servicesAmaClassesStarted.add(newService);
	}
	
	/**
	 * Changement d'adresse du serveur FTP
	 * @param urlClass : url menant au dossier classes
	 * @param urlLib : url menant au dossier lib
	 */
	public static void changeFTPServer(String urlClass, String urlLib) {
		serverFTPURLClass = urlClass;
		serverFTPURLLib = urlLib;
	}
	
	/**
	 * Désinstallation d'un service arreté
	 * @param serviceToRemove
	 */
	public static void removeService(Class<?> serviceToRemove){
		servicesAmaClassesStopped.remove(serviceToRemove);	
	}
	
	/**
	 * Arrêt d'un service déployé ne le désinstalle pas
	 * @param serviceToStop
	 */
	public static void stopService(Class<?> serviceToStop) {
		servicesAmaClassesStarted.remove(serviceToStop);
		servicesAmaClassesStopped.add(serviceToStop);
	}
	
	/**
	 * Démarrage d'un service en attente de déploiement
	 * @param serviceToStart
	 */
	public static void startService(Class<?> serviceToStart) {
		servicesAmaClassesStopped.remove(serviceToStart);
		servicesAmaClassesStarted.add(serviceToStart);
	}
	
	/**
	 * Mise à jour d'un service en attente de déploiement
	 * @param updatedService
	 * @param login
	 */
	public static void updateService(Class<?>updatedService, String login) {
		/* on recherche dans la liste des services arrêté, la class qui a le même nom que le service mis à jour */
		for(Class<?> service : servicesAmaClassesStopped) {
			if(service.getSimpleName().equals(updatedService.getSimpleName())){
				servicesAmaClassesStopped.remove(service);
				try {
					ServiceRegistry.addService(updatedService, login);
				} catch (AddServiceException e) {
					
				}
				return;
			}
		}
	}
	
	
	/**
	 *  renvoie la classe de service d'index numService - 1 (les index commençant à 0 et l'affichage commençant à 1)
	 * @param numService
	 * @return
	 */
	public static Class<?> getServiceClass(int numService) {
		return servicesAmaClassesStarted.get(numService - 1);
	}
	
	public static Class<?> getServiceStoppedClass(int numService){
		return servicesAmaClassesStopped.get(numService - 1);
	}
	
	/* booléen qui renvoit si aucun service n'a été ajouté */
	public static boolean noServiceInstalled() {
		return servicesAmaClassesStopped.size() == 0 && servicesAmaClassesStarted.size() == 0;
	}
	
	
	
// liste les activités présentes ET démarrer
	public static String toStringue() {
		String result = "Activités présentes : ##";
		int cpt = 1;
		for(Class<?> c : servicesAmaClassesStarted)
			result += cpt++ + ": "+ c.getSimpleName() + "##";
		
		return result;
	}
	
	public static String toStringueStopped() {
		String result = "Activités arrêtés présentes : ##";
		int cpt = 1;
		for(Class<?> c : servicesAmaClassesStopped)
			result += cpt++ + ": "+ c.getSimpleName() + "##";
		
		return result;
	}
	
	private static boolean checkInterface(Class<?>[] interfaces) {
		boolean impServiceBri = false;
		if(interfaces.length > 0) {
			for(Class<?> c : interfaces) {
				if(c.equals(Service.class)) {
					impServiceBri=true;
					break;
				}
			}
		}
		return impServiceBri;
	}
	
	private static boolean checkServPackage(String login, String packageName) {
		return login.equals(packageName);
	}
	
	private static boolean hasPublicConstructor(Class<?> classe) {
		try {
			Constructor<?> c = classe.getConstructor(Socket.class);
			int cMod = c.getModifiers();
			if(Modifier.isPublic(cMod)) {
				return true;
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean hasSocketAttr(Class<?> classe) {
		boolean isConform = false;
		Field[] fields = classe.getDeclaredFields();
		
		for(Field f : fields) {
			if(f.getType().equals(Socket.class)) {
				isConform = true;
				break;
			}
		}
		return isConform;
	}
	
	private static boolean hasToString(Class<?> classe) {
		boolean isConform = false;
		Method[] methods = classe.getMethods();
		
		for(Method m : methods) {
			if(m.getName().equals("toStringue")) {
				int mMod = m.getModifiers();
				if(Modifier.isPublic(mMod) && Modifier.isStatic(mMod) && m.getReturnType().equals(String.class)) {
					isConform = true;
				}
				break;
			}
		}
		return isConform;
	}
	
	/**
	 * Initialisation des services programmeurs, n'est appelé qu'une seule fois
	 * @throws Exception
	 */
	private static void initializeProgServices() throws Exception {
		servicesProgClasses.add(Class.forName("servicesProg.ServiceAjout"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceArrêt"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceChangementServeurFTP"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceDémarrer"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceDésinstaller"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceMiseAJour"));
	}
	
	public static Class<?> getServicesClassesProg(int numService) {
		return servicesProgClasses.get(numService - 1);
	}
	
	public static String toStringueProg() {
		String result = "Fonctionnalités programmeur : ##";
		int cpt = 1;
		for(Class<?> c : servicesProgClasses)
			result += cpt++ + ": "+ c.getSimpleName() + "##";
		
		return result;
	}

}
