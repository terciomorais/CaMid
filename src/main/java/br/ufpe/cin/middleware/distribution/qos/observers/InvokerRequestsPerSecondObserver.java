package br.ufpe.cin.middleware.distribution.qos.observers;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicLong;

import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgent;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.SynchronizedMetric;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.Timer;

public class InvokerRequestsPerSecondObserver extends TickableObserver
{
	private LocalAgent localAgent;
	
	private AtomicLong requestsPerSecond = new AtomicLong(0);
	
	public InvokerRequestsPerSecondObserver()
	{
		localAgent = LocalAgent.getInstance();
	}
	
	public synchronized void update(Observable eventSource, Object event) 
	{
		if(eventSource instanceof Timer )
		{
			SynchronizedMetric invokerRPS = localAgent.getRequestsPerSecondMetricForInvoker();
			invokerRPS.addMetric(requestsPerSecond.getAndSet(0));
		}
		else
		{
			requestsPerSecond.incrementAndGet();			
		}
	}
	
}
