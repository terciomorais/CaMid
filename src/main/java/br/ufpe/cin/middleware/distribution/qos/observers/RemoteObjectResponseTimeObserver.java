package br.ufpe.cin.middleware.distribution.qos.observers;
import java.util.Observable;
import java.util.Observer;

import br.ufpe.cin.middleware.distribution.ObjectDescriptor;
import br.ufpe.cin.middleware.distribution.qos.events.RemoteObjectEvent;
import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgent;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.ClientInvocationContext;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.SynchronizedMetric;


public class RemoteObjectResponseTimeObserver implements Observer
{
	private LocalAgent localAgent;
	
	public RemoteObjectResponseTimeObserver()
	{
		localAgent = LocalAgent.getInstance();
	}
	
	public void update(Observable arg0, Object arg1) 
	{
		RemoteObjectEvent remoteObjectEvent = (RemoteObjectEvent) arg1;
		ObjectDescriptor descriptor = remoteObjectEvent.getDescriptor();
		String serviceName = descriptor.getServiceName();
//		String className = descriptor.getClassType().getName();
		String methodName = descriptor.getMethod().getValue();
		ClientInvocationContext methodContext = new ClientInvocationContext(serviceName, methodName);
		
		ClientInvocationContext objectContext = new ClientInvocationContext(serviceName, null);
		
		SynchronizedMetric remoteObjectRTM = 
			localAgent.getResponseTimeMetricForRemoteObject( methodContext.toString() );
		remoteObjectRTM.addMetric(remoteObjectEvent.getElapsedTime());
		
		remoteObjectRTM = 
			localAgent.getResponseTimeMetricForRemoteObject( objectContext.toString() );
		remoteObjectRTM.addMetric(remoteObjectEvent.getElapsedTime());
	}
	
}
