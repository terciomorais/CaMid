package br.ufpe.cin.in1118.services.commons.naming;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import br.ufpe.cin.in1118.utils.PropertiesSetup;

public class LocalServiceRegistry {

	private static	LocalServiceRegistry	INSTANCE			= null;
	private 		Map<String, Class<?>>	serviceRegistry		= null;
	private 		PropertiesSetup			remoteProperties	= new PropertiesSetup("/root/config/remote.properties");

	public LocalServiceRegistry() {
		this.serviceRegistry = new TreeMap<String, Class<?>>();
	}
	
	public LocalServiceRegistry(String propFile){
		this.remoteProperties	= new PropertiesSetup(propFile);
		this.serviceRegistry	= new TreeMap<String, Class<?>>();
	}
	
	public PropertiesSetup getRemoteProperties() {
		return remoteProperties;
	}
	
	public void setRemoteProperties(String props){
		this.remoteProperties = new PropertiesSetup(props);
	}
	
	public static LocalServiceRegistry getINSTANCE(){
		if (LocalServiceRegistry.INSTANCE == null)
			LocalServiceRegistry.INSTANCE = new LocalServiceRegistry();
		return LocalServiceRegistry.INSTANCE;
	}
	
	public static LocalServiceRegistry getINSTANCE(String propsFile){
		if (LocalServiceRegistry.INSTANCE == null)
			LocalServiceRegistry.INSTANCE = new LocalServiceRegistry(propsFile);
		return LocalServiceRegistry.INSTANCE;
	}
	
	public void registerService(String serviceName, Class<?> remoteObjectClass)	{
		this.serviceRegistry.put(serviceName, remoteObjectClass);
	}

	public Class<?> getRemoteObjectClass(String serviceName) {
		return this.serviceRegistry.get(serviceName);
	}

	public Set<String> getAllServiceNames()	{
		return this.serviceRegistry.keySet();
	}

	public Map<String, Class<?>> getAllServices(){
		return this.serviceRegistry;
	}
	
	public void addService(String serviceName, Class<?> clazz){
		this.serviceRegistry.put(serviceName, clazz);
	}
	
	public void removeService(String serviceName){
		this.serviceRegistry.remove(serviceName);
	}
	
	public void create(){

		LocalServiceRegistry registry = LocalServiceRegistry.getINSTANCE();
		@SuppressWarnings("unchecked")
		Enumeration<String> services
			= (Enumeration<String>) registry.getRemoteProperties().getProperties().propertyNames();
		while(services.hasMoreElements()){
			String serviceName = services.nextElement();
			String className
				= registry.getRemoteProperties().getProperties().getProperty(serviceName);
			try {
				Class<?> t = Class.forName(className);
				registry.registerService(serviceName, t);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}	
	}
	//Reading class service from the property file service.properties
	public static LocalServiceRegistry createDefault(){

		LocalServiceRegistry registry = LocalServiceRegistry.getINSTANCE();
		@SuppressWarnings("unchecked")
		Enumeration<String> services
			= (Enumeration<String>) registry.getRemoteProperties().getProperties().propertyNames();
		while(services.hasMoreElements()){
			String serviceName = services.nextElement();
			String className
				= registry.getRemoteProperties().getProperties().getProperty(serviceName);
			try {
				Class<?> t = Class.forName(className);
				registry.registerService(serviceName, t);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}	
		return registry;
	}

}
