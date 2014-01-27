package br.ufpe.cin.middleware.distribution.qos.events;

import br.ufpe.cin.middleware.distribution.ObjectDescriptor;
import br.ufpe.cin.middleware.distribution.qos.QOSEventSource;

public class RemoteObjectEventSource extends QOSEventSource {

	public RemoteObjectEventSource() {
		super(new RemoteObjectEvent());
		// TODO Auto-generated constructor stub
	}
	
	public void startTimer()
	{
		long startTime = System.currentTimeMillis();
		getEvent().setStartTime(startTime);
	}
	
	public void stopTimer()
	{
		Long endTime = System.currentTimeMillis();
		getEvent().setEndTime(endTime);
	}
	
	public void notifyObservers()
	{
		setChanged();
		notifyObservers(event);
	}

	public void setException(Exception e) 
	{
		getEvent().setException(e);
	}
	
	public RemoteObjectEvent getEvent()
	{
		return (RemoteObjectEvent) this.event;
	}
	
	private ObjectDescriptor descriptor;
	
	public void setObjectDescription(ObjectDescriptor descriptor)
	{
		getEvent().setDescriptor(descriptor);
	}
	
	
	

}
