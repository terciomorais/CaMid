package br.ufpe.cin.middleware.distribution.qos.events;

import br.ufpe.cin.middleware.distribution.qos.QOSEventSource;



public class InvokerEventSource extends QOSEventSource {
	
	
	public InvokerEventSource()	
	{
		super( new InvokerEvent() );
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
	
	public InvokerEvent getEvent()
	{
		return (InvokerEvent) this.event;
	}
	
}
