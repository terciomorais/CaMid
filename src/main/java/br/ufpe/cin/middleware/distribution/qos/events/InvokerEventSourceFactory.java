package br.ufpe.cin.middleware.distribution.qos.events;

import br.ufpe.cin.middleware.distribution.qos.QOSEventSourceFactory;

public class InvokerEventSourceFactory extends QOSEventSourceFactory {

	private static InvokerEventSourceFactory INSTANCE;
	
	public synchronized static InvokerEventSourceFactory getInstance()  
	{
		if(INSTANCE == null)
		{
			INSTANCE = new InvokerEventSourceFactory();
		}
		return INSTANCE;
	}
	
	public InvokerEventSource createEventSource() 
	{
		InvokerEventSource ies = new InvokerEventSource();
		this.addObservers(ies);
		return ies;
	}

}
