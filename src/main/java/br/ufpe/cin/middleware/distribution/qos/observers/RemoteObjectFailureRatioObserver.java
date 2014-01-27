package br.ufpe.cin.middleware.distribution.qos.observers;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicLong;

import br.ufpe.cin.middleware.distribution.ObjectDescriptor;
import br.ufpe.cin.middleware.distribution.qos.events.RemoteObjectEvent;
import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgent;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.ClientInvocationContext;

public class RemoteObjectFailureRatioObserver implements Observer{

private LocalAgent localAgent;
	
	private AtomicLong total = new AtomicLong(0);
	private AtomicLong failures = new AtomicLong(0);

	public RemoteObjectFailureRatioObserver()
	{
		localAgent = LocalAgent.getInstance();
	}
	
	public void update(Observable arg0, Object arg1) 
	{
		RemoteObjectEvent invokerEvent = (RemoteObjectEvent) arg1;
		Long total = this.total.incrementAndGet();
		ObjectDescriptor descriptor = invokerEvent.getDescriptor();
		String serviceName = descriptor.getServiceName();
//		String className = descriptor.getClassType().getName();
		String methodName = descriptor.getMethod().getValue();
		ClientInvocationContext methodContext = new ClientInvocationContext(serviceName, methodName);
		ClientInvocationContext objectContext = new ClientInvocationContext(serviceName, null);
		
		if(invokerEvent.getException() != null)
		{
			System.out.println("Falha.................");
		}
		
		Long failures = invokerEvent.getException() != null ?
				this.failures.incrementAndGet() : this.failures.get();
		
		AtomicLong value = localAgent.getFailureRatioForRemoteObject(methodContext.toString());
		value.getAndSet(Double.doubleToLongBits(
			failures.doubleValue() / total.doubleValue() 	
		));  
		
		value = localAgent.getFailureRatioForRemoteObject(objectContext.toString());
		value.getAndSet(Double.doubleToLongBits(
			failures.doubleValue() / total.doubleValue() 	
		));
		
	}
	
}
