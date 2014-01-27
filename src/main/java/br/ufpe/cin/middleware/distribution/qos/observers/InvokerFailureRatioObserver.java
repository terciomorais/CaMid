package br.ufpe.cin.middleware.distribution.qos.observers;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicLong;

import br.ufpe.cin.middleware.distribution.qos.events.InvokerEvent;
import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgent;

public class InvokerFailureRatioObserver implements Observer {

	private AtomicLong total = new AtomicLong(0);
	private AtomicLong failures = new AtomicLong(0);

	private LocalAgent localAgent;
	
	public InvokerFailureRatioObserver()
	{
		localAgent = LocalAgent.getInstance();
	}
	
	public void update(Observable arg0, Object arg1) 
	{
		InvokerEvent invokerEvent = (InvokerEvent) arg1;
		Long total = this.total.incrementAndGet();
		
		Long failures = invokerEvent.getException() != null ?
				this.failures.incrementAndGet() : this.failures.get();
		
		AtomicLong value = localAgent.getFailureRatioForInvoker();
		value.getAndSet(Double.doubleToLongBits(
			failures.doubleValue() / total.doubleValue() 	
		));  
		
	}
	
}
