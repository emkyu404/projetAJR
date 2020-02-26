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
		servicesAmaClasses = new Vector<Class<?>>();
		servicesProgClasses = new Vector<Class<?>>(); 
		try{
			initializeProgServices();
		}catch(Exception e) {
			
		}
	}
	private static List<Class<?>> servicesAmaClasses;
	private static List<Class<?>> servicesProgClasses;
// ajoute une classe de service après contrôle de la norme BLTi
	public static void addService(Class<?> newService) throws AddServiceException {
	
		int modifier = newService.getModifiers();
		Class<?>[] interfaces = newService.getInterfaces();
		
		if(!checkInterface(interfaces)) throw new AddServiceException("Le service n'implemente pas la classe ServiceBRi");
		
		if(!Modifier.isPublic(modifier)) throw new AddServiceException("Le service n'est pas une classe publique");
		
		if(Modifier.isAbstract(modifier)) throw new AddServiceException("Le service est une classe abstraite");
		
		if(!hasPublicConstructor(newService)) throw new AddServiceException("Le service n'a pas de constructeur publique avec en paramètre un Socket");
		
		if(!hasSocketAttr(newService)) throw new AddServiceException("Le service n'a pas de Socket en attribut");
		
		if(!hasToString(newService)) throw new AddServiceException("Le service n'a pas de méthode 'public static String ToStringue()'");
		
		servicesAmaClasses.add(newService);
	}
	
	public static void removeService(Class<?> serviceToRemove){
		servicesAmaClasses.remove(serviceToRemove);
	}
	
// renvoie la classe de service (numService -1)	
	public static Class<?> getServiceClass(int numService) {
		return servicesAmaClasses.get(numService - 1);
	}
	
// liste les activités présentes
	public static String toStringue() {
		String result = "Activités présentes : ##";
		int cpt = 1;
		for(Class<?> c : servicesAmaClasses)
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
		//servicesProg.add(Class.forName("servicesProg.ServiceArrêt"));
		//servicesProg.add(Class.forName("servicesProg.ServiceChangementServeurFTP"));
		//servicesProg.add(Class.forName("servicesProg.ServiceDémarrer"));
		//servicesProg.add(Class.forName("servicesProg.ServiceDésinstaller"));
		//servicesProg.add(Class.forName("servicesProg.ServiceMiseAJour"));
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
