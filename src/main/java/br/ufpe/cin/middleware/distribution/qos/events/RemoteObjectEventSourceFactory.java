package br.ufpe.cin.middleware.distribution.qos.events;

import br.ufpe.cin.middleware.distribution.qos.QOSEventSourceFactory;

public class RemoteObjectEventSourceFactory extends QOSEventSourceFactory
{
	private static RemoteObjectEventSourceFactory INSTANCE;
	
	public synchronized static RemoteObjectEventSourceFactory getInstance()  
	{
		if(INSTANCE == null)
		{
			INSTANCE = new RemoteObjectEventSourceFactory();
		}
		return INSTANCE;
	}
	
	public RemoteObjectEventSource createEventSource() 
	{
		RemoteObjectEventSource ies = new RemoteObjectEventSource();
		this.addObservers(ies);
		return ies;
	}
}
