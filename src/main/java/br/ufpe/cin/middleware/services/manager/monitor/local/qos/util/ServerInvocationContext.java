package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;

import br.ufpe.cin.middleware.services.LocalServiceRegistry;



public class ServerInvocationContext {

	private ClientInvocationContext clientContext;
	
	private Class<?> localObjectClass;
	
	public ServerInvocationContext(String rawString, LocalServiceRegistry registry)
	{
		clientContext = new ClientInvocationContext(rawString);
		localObjectClass = registry.getRemoteObjectClass(clientContext.getServiceName());
	}
	
	
	
}
