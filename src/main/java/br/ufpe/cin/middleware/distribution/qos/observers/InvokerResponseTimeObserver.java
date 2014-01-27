package br.ufpe.cin.middleware.distribution.qos.observers;

import java.util.Observable;
import java.util.Observer;

import br.ufpe.cin.middleware.distribution.qos.events.InvokerEvent;
import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgent;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.SynchronizedMetric;

public class InvokerResponseTimeObserver implements Observer {

	private LocalAgent localAgent;
	
	public InvokerResponseTimeObserver()
	{
		localAgent = LocalAgent.getInstance();
	}
	
	public void update(Observable arg0, Object arg1) 
	{
		InvokerEvent invokerEvent = (InvokerEvent) arg1;
		SynchronizedMetric remoteObjectRTM = localAgent.getResponseTimeMetricForInvoker();
		remoteObjectRTM.addMetric(invokerEvent.getElapsedTime());
	}
	
	
}
