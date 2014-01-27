package br.ufpe.cin.middleware.services;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.ufpe.cin.middleware.distribution.remote.Calculadora;
import br.ufpe.cin.middleware.distribution.remote.Delay;
import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgentRemoteObject;
//import br.ufpe.cin.middleware.distribution.cloud.CloudProxy;

public class LocalServiceRegistry {

	private Map<String, Class<?>> serviceRegistry;
	
	public LocalServiceRegistry()
	{
		serviceRegistry = new TreeMap<String,Class<?>>();
	}
	
	public void registerService(String serviceName, Class<?> remoteObjectClass)
	{
		this.serviceRegistry.put(serviceName, remoteObjectClass);
	}
	
	public Class<?> getRemoteObjectClass(String serviceName)
	{
		return this.serviceRegistry.get(serviceName);
	}
	
	public Set<String> getAllServices()
	{
		return this.serviceRegistry.keySet();
	}
	
	public static LocalServiceRegistry createDefault()
	{
		LocalServiceRegistry registry = new LocalServiceRegistry();
		registry.registerService(LocalAgentRemoteObject.SERVICE_NAME, LocalAgentRemoteObject.class);
		registry.registerService(Calculadora.SERVICE_NAME, Calculadora.class);
		registry.registerService(Delay.SERVICE_NAME, Delay.class);
		//registry.registerService(CloudProxy.SERVICE_NAME, CloudProxy.class);
		
		return registry;
	}
}
