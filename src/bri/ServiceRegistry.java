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
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesAmaClassesStarted = new Vector<Class<?>>();
		servicesAmaClassesStopped = new Vector<Class<?>>();
		servicesProgClasses = new Vector<Class<?>>(); 
		try{
			initializeProgServices();
		}catch(Exception e) {
			
		}
	}
	
	private static String serverFTPURL;
	private static List<Class<?>> servicesAmaClassesStarted;
	private static List<Class<?>> servicesAmaClassesStopped;
	private static List<Class<?>> servicesProgClasses;
// ajoute une classe de service après contrôle de la norme BLTi
	public static void addService(Class<?> newService, String loginProg) throws AddServiceException {
	
		int modifier = newService.getModifiers();
		Class<?>[] interfaces = newService.getInterfaces();
		if(checkServPackage(loginProg, newService.getPackageName()))
		
		if(!checkInterface(interfaces)) throw new AddServiceException("Le service n'implemente pas la classe ServiceBRi");
		
		if(!Modifier.isPublic(modifier)) throw new AddServiceException("Le service n'est pas une classe publique");
		
		if(Modifier.isAbstract(modifier)) throw new AddServiceException("Le service est une classe abstraite");
		
		if(!hasPublicConstructor(newService)) throw new AddServiceException("Le service n'a pas de constructeur publique avec en paramètre un Socket");
		
		if(!hasSocketAttr(newService)) throw new AddServiceException("Le service n'a pas de Socket en attribut");
		
		if(!hasToString(newService)) throw new AddServiceException("Le service n'a pas de méthode 'public static String ToStringue()'");
		
		servicesAmaClassesStarted.add(newService);
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
	
	public static void updateService(Class<?>updatedService) {
		/* on recherche dans la liste des services arrêté, la class qui a le même nom que le service mis à jour */
		for(Class<?> service : servicesAmaClassesStopped) {
			if(service.getSimpleName().equals(updatedService.getSimpleName())){
				servicesAmaClassesStopped.remove(service);
				servicesAmaClassesStopped.add(updatedService);
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
	
	private static void initializeProgServices() throws Exception {
		servicesProgClasses.add(Class.forName("servicesProg.ServiceAjout"));
		servicesProgClasses.add(Class.forName("servicesProg.ServiceArrêt"));
		//servicesProg.add(Class.forName("servicesProg.ServiceChangementServeurFTP"));
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
