package br.ufpe.cin.middleware.distribution.qos.observers;

import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;

import br.ufpe.cin.middleware.distribution.ObjectDescriptor;
import br.ufpe.cin.middleware.distribution.qos.events.RemoteObjectEvent;
import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgent;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.ClientInvocationContext;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.SynchronizedMetric;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.Timer;

public class RemoteObjectRequestsPerSecondObserver extends TickableObserver{
	
	private LocalAgent localAgent;
	
	private Map<String, Long> rpsContextMap;
	
	public RemoteObjectRequestsPerSecondObserver()
	{
		localAgent = LocalAgent.getInstance();
		rpsContextMap = new TreeMap<String, Long>();
	}
	
	public synchronized void update(Observable eventSource, Object event) 
	{
		Long requestsPerSecond = null;
		
		if(eventSource instanceof Timer )
		{
			Set<String> contextSet = rpsContextMap.keySet();
			for(String context : contextSet)
			{
				requestsPerSecond = rpsContextMap.get(context);
				SynchronizedMetric remoteObjectRPS = localAgent.getRequestsPerSecondMetricForRemoteObject( context );
				remoteObjectRPS.addMetric(requestsPerSecond == null ? 0 : requestsPerSecond);
				requestsPerSecond = rpsContextMap.get(context);
			}
		}
		else
		{
			RemoteObjectEvent invokerEvent = (RemoteObjectEvent) event;
			ObjectDescriptor descriptor = invokerEvent.getDescriptor();
			String serviceName = descriptor.getServiceName();
//			String className = descriptor.getClassType().getName();
			String methodName = descriptor.getMethod().getValue();
			ClientInvocationContext methodContext = new ClientInvocationContext(serviceName, methodName);
			ClientInvocationContext objectContext = new ClientInvocationContext(serviceName, null);
			
			requestsPerSecond  = rpsContextMap.get(methodContext.toString());
			rpsContextMap.put(methodContext.toString(), requestsPerSecond == null ? 1 : requestsPerSecond.longValue()+1 );
			
			requestsPerSecond  = rpsContextMap.get(objectContext.toString());
			rpsContextMap.put(objectContext.toString(), requestsPerSecond == null ? 1 : requestsPerSecond.longValue()+1 );
		}
	}

}
