package br.ufpe.cin.middleware.distribution.qos.events;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.qos.QOSEventSource;

public class ReceiverEventSource extends QOSEventSource {

	public ReceiverEventSource() 
	{
		super(new ReceiverEvent());		
	}

	public ReceiverEvent getEvent()
	{
		return (ReceiverEvent) this.event;
	}
	
	public void startTimer()
	{
		long start = System.currentTimeMillis();
		getEvent().setStartTime(start);
	}
	
	public void endTimer()
	{
		long end = System.currentTimeMillis();
		getEvent().setEndTime(end);
	}
	
	public void setRequestMessage(MethodRequestMessage req)
	{
		getEvent().setRequestMessage(req);
	}
	
}

