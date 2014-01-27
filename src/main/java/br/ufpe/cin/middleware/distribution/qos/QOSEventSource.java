package br.ufpe.cin.middleware.distribution.qos;

import java.util.Observable;

public class QOSEventSource extends Observable {

	protected Object event;
	
	public QOSEventSource(Object event)
	{
		this.event = event;
	}
	
	protected void finalize()
	{
		this.deleteObservers();
	}
	
	public Object getEvent()
	{
		return event;
	}
	
	
	public static void main(String[] args) 
	{
		QOSEventSource eventSource = new QOSEventSource("asasdas");
		System.out.println(eventSource.getEvent());
	}
	
	
}
