package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import servicesProg.*;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partag�e en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesAmaClassesStarted = new Vector<Class<?>>();
		servicesAmaClassesStopped = new Vector<Class<?>>();
		servicesProgClasses = new Vector<Class<?>>(); 
		
		serverFTPURLClass = "ftp://localhost:2121/tp4/classes/";
		serverFTPURLLib = "ftp://localhost:2121/tp4/lib/";
		try{
			initializeProgServices();
		}catch(Exception e) {
			
		}
	}
	
	private static String serverFTPURLClass;
	private static String serverFTPURLLib;
	private static List<Class<?>> servicesAmaClassesStarted;
	private static List<Class<?>> servicesAmaClassesStopped;
	private static List<Class<?>> servicesProgClasses;
	
	/* permet de r�cup�rer l'adresse du serveurFTP */
	public static String getServerFTPURLClass() {
		return serverFTPURLClass;
	}
	/* permet de r�cupererl 'adresse du serveur FTP */
	public static String getServerFTPURLLib() {
		return serverFTPURLLib;
	}
	
	
	// ajoute une classe de service apr�s contr�le de la norme BLTi
	public static void addService(Class<?> newService, String loginProg) throws AddServiceException {
	
		int modifier = newService.getModifiers();
		Class<?>[] interfaces = newService.getInterfaces();
		if(!checkServPackage(loginProg, newService.getPackageName())) throw new AddServiceException("Le service que vous souhaitez impl�ment� ne vous appartient pas");
		
		if(!checkInterface(interfaces)) throw new AddServiceException("Le service n'implemente pas la classe ServiceBRi");
		
		if(!Modifier.isPublic(modifier)) throw new AddServiceException("Le service n'est pas une classe publique");
		
		if(Modifier.isAbstract(modifier)) throw new AddServiceException("Le service est une classe abstraite");
		
		if(!hasPublicConstructor(newService)) throw new AddServiceException("Le service n'a pas de constructeur publique avec en param�tre un Socket");
		
		if(!hasSocketAttr(newService)) throw new AddServiceException("Le service n'a pas de Socket en attribut");
		
		if(!hasToString(newService)) throw new AddServiceException("Le service n'a pas de m�thode 'public static String ToStringue()'");
		
		/* recherche dans la liste des classes de services arr�t�s si le service existe d�j� */
		for(Class<?> service : servicesAmaClassesStopped) {
			if(service.getSimpleName().equals(newService.getSimpleName())){
				throw new AddServiceException("Le service que vous cherchez � impl�menter existe d�j�. Utiliser la fonctionnalit� de mise � jour.");
			}
		}
		
		/* recherche dans la liste des classes de services d�ploy�s si le service existe d�j� */
		for(Class<?> service : servicesAmaClassesStarted) {
			if(service.getSimpleName().equals(newService.getSimpleName())){
				throw new AddServiceException("Le service que vous cherchez � impl�menter existe d�j�. Utiliser la fonctionnalit� de mise � jour.");
			}
		}
		servicesAmaClassesStarted.add(newService);
	}
	
	
	//changement de l'adresse du serveur
	public static void changeFTPServer(String urlClass, String urlLib) {
		serverFTPURLClass = urlClass;
		serverFTPURLLib = urlLib;
	}
	
	
	public static void removeService(Class<?> serviceToRemove){
		servicesAmaClassesStopped.remove(serviceToRemove);	
	}
	
	public static void stopService(Class<?> serviceToStop) {
		servicesAmaClassesStarted.remove(serviceToStop);
		servicesAmaClassesStopped.add(serviceToStop);
	}
	
	public static void startService(Class<?> serviceToStart) {
		servicesAmaClassesStopped.remove(serviceToStart);
		servicesAmaClassesStarted.add(serviceToStart);
	}
	
	public static void updateService(Class<?>updatedService, String login) {
		/* on recherche dans la liste des services arr�t�, la class qui a le m�me nom que le service mis � jour */
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
	
	
// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) {
		return servicesAmaClassesStarted.get(numService - 1);
	}
	
	public static Class<?> getServiceStoppedClass(int numService){
		return servicesAmaClassesStopped.get(numService - 1);
	}
	
	/* bool�en qui renvoit si aucun service n'a �t� ajout� */
	public static boolean noServiceInstalled() {
		return servicesAmaClassesStopped.size() == 0 && servicesAmaClassesStarted.size() == 0;
	}
	
	
	
// liste les activit�s pr�sentes ET d�marrer
	public static String toStringue() {
		String result = "Activit�s pr�sentes : ##";
		int cpt = 1;
		for(Class<?> c : servicesAmaClassesStarted)
			result += cpt++ + ": "+ c.getSimpleName() + "##";
		
		return result;
	}
	
	public static String toStringueStopped() {
		String result = "Activit�s arr�t�s pr�sentes : ##";
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
	
	private static void initializeProgServices() throws Exception {
		servicesProgClasses.add(Class.forName("servicesProg.ServiceAjout"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceArr�t"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceChangementServeurFTP"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceD�marrer"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceD�sinstaller"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceMiseAJour"));
	}
	
	public static Class<?> getServicesClassesProg(int numService) {
		return servicesProgClasses.get(numService - 1);
	}
	
	public static String toStringueProg() {
		String result = "Fonctionnalit�s programmeur : ##";
		int cpt = 1;
		for(Class<?> c : servicesProgClasses)
			result += cpt++ + ": "+ c.getSimpleName() + "##";
		
		return result;
	}

}
