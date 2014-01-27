package br.ufpe.cin.middleware.distribution.qos;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;


public abstract class QOSEventSourceFactory {

	
	protected List<Observer> observers;
	
	protected boolean enabled;
	
	public QOSEventSourceFactory()
	{
		observers = new ArrayList<Observer>();
	}
	
	public void registerObserver(Observer observer)
	{
		observers.add(observer);
	}
	
	public void unregisterObserver(Observer observer)
	{
		observers.remove(observer);
	}
	
	protected void addObservers(QOSEventSource eventSource)
	{
		for(Observer observer : observers)
		{
			eventSource.addObserver(observer);
		}
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public abstract QOSEventSource createEventSource();
}
