package br.ufpe.cin.middleware.services;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class LocalServiceRegistry {

	private Map<String, Class<?>> serviceRegistry;
	private PropertiesSetup remoteProperties = new PropertiesSetup("/remote.properties");
	
	public PropertiesSetup getRemoteProperties() {
		return remoteProperties;
	}

	public LocalServiceRegistry() {
		serviceRegistry = new TreeMap<String,Class<?>>();
	}
	
	public void registerService(String serviceName, Class<?> remoteObjectClass)	{
		
		this.serviceRegistry.put(serviceName, remoteObjectClass);
	}
	
	public Class<?> getRemoteObjectClass(String serviceName) {
		return this.serviceRegistry.get(serviceName);
	}
	
	public Set<String> getAllServices()	{
		return this.serviceRegistry.keySet();
	}
	
	//Reading class service from the property file service.properties
	public static LocalServiceRegistry createDefault(){
		
		LocalServiceRegistry registry = new LocalServiceRegistry();
		@SuppressWarnings("unchecked")
		Enumeration<String> services = (Enumeration<String>) registry.getRemoteProperties().getProperties().propertyNames();
		while(services.hasMoreElements()){
			String serviceName = services.nextElement();
			String className = registry.getRemoteProperties().getProperties().getProperty(serviceName);
			try {
				Class<?> t = Class.forName(className);
				registry.registerService(serviceName, t);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return registry;
	}
}
