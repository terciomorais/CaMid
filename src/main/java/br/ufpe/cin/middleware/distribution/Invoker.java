package br.ufpe.cin.middleware.distribution;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;
import br.ufpe.cin.middleware.distribution.protocol.RemoteVoid;
import br.ufpe.cin.middleware.distribution.protocol.RequestParameter;
import br.ufpe.cin.middleware.distribution.qos.events.InvokerEventSource;
import br.ufpe.cin.middleware.distribution.qos.events.InvokerEventSourceFactory;
import br.ufpe.cin.middleware.distribution.qos.events.RemoteObjectEventSource;
import br.ufpe.cin.middleware.distribution.qos.events.RemoteObjectEventSourceFactory;
import br.ufpe.cin.middleware.services.LocalServiceRegistry;

public class Invoker {
	
	private Serializable primitiveMessage = null;
	private MethodRequestMessage requestMessage;
	private MethodResponseMessage responseMessage;
	private ObjectDescriptor objDescription;
	private InvokerEventSourceFactory iesFactory;
	private RemoteObjectEventSourceFactory roesFactory;
	private LocalServiceRegistry registry;
	
	private boolean returnable = true;
	//private LocalServiceRegistry registry;
	
	public boolean isReturnable() {
		return returnable;
	}

	public Invoker(MethodRequestMessage receivedMessage, LocalServiceRegistry registry) {
		this.requestMessage = receivedMessage;
		this.registry = registry;
		this.iesFactory = InvokerEventSourceFactory.getInstance();
		this.roesFactory = RemoteObjectEventSourceFactory.getInstance();
	}
	
	public Serializable getPrimitiveMessage() {
		return primitiveMessage;
	}
	
	public void setPrimitiveMessage(Serializable primitiveMessage) {
		this.primitiveMessage = primitiveMessage;
	}
	
//	private static AtomicInteger invocationCounter = new AtomicInteger(0);
	
	public void processMessage() {
		InvokerEventSource eventSource = null;
		if(iesFactory.isEnabled()) {
			eventSource = (InvokerEventSource) iesFactory.createEventSource();
			eventSource.startTimer();
		}
		try	{
			String serviceName = this.requestMessage.getServiceName();
			
			System.out.println("Invoking service " + serviceName);
			
			Class<?> remoteObjectClass = registry.getRemoteObjectClass(serviceName);
			
//			this.remoteObject = remoteObjectClass.newInstance();
			
			this.objDescription = new ObjectDescriptor();
			this.objDescription.setServiceName(this.requestMessage.getServiceName());
			this.objDescription.setClassType(remoteObjectClass);
			Parameter method = new Parameter(serviceName, requestMessage.getMethod(), true);
			this.objDescription.setMethod(method);
			this.objDescription.setParameters(this.requestMessage.getParameters());
			
//			eventSource.setObjectDescription(this.objDescription);

			this.responseMessage = this.execute();
			
			if(iesFactory.isEnabled()) {
				eventSource.stopTimer();
				if(this.responseMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION)) {
					eventSource.setException((Exception)this.responseMessage.getResponse());
				}
				eventSource.notifyObservers();
			}
		}
		catch(Exception e){
			if(iesFactory.isEnabled()) {
				eventSource.stopTimer();
//				System.err.println("Error in the " + invocationCounter.get() + "th interation");
				eventSource.setException(e);
				eventSource.notifyObservers();
			}
			throw new RuntimeException(e);
		}
	}

	public MethodResponseMessage getResponseMessage() {
		return responseMessage;
	}
	
	public MethodResponseMessage execute() {
		RemoteObjectEventSource roes = null;
		if(roesFactory.isEnabled())	{
			roes = roesFactory.createEventSource();
		}
		MethodResponseMessage response = new MethodResponseMessage();
		try {
			
			Serializable result = null;
			Object object = this.objDescription.getClassType().newInstance();
			if(roesFactory.isEnabled())	{
				roes.setObjectDescription(objDescription);
			}
			
			Object[] params = new Object[this.objDescription.getParameters().size()];
			
			List<RequestParameter> requestParameters = this.objDescription.getParameters();
			for(int i = 0; i < this.objDescription.getParameters().size(); i++)	{
				params[i] = requestParameters.get(i).getValue();
			}
			
			Method[] methods = this.objDescription.getClassType().getMethods();
			
			for(Method method : methods) {
				//				System.out.printf("%s::%s\n", object.getClass().getName(), methodName);
				if(method.getName().equals(this.objDescription.getMethod().getValue())) {
					if(roesFactory.isEnabled())	{
						roes.startTimer();
					}
					result = (Serializable) method.invoke(object, params);
					if(roesFactory.isEnabled())	{
						roes.stopTimer();
						roes.notifyObservers();
					}
					
					if(method.getReturnType().equals(Void.TYPE)) {
						response.setResponse(new RemoteVoid());
					}
					else {
						response.setResponse(result);
					}
					response.setStatus(MethodResponseMessage.ResponseStatus.SUCCESS);
					response.setStatusMessage("OK");
				}
			}
			
		} catch (InstantiationException e) {
			response.setResponse(e);
			response.setStatus(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION);
			response.setStatusMessage("SERVICE FAULT");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			response.setResponse(e);
			response.setStatus(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION);
			response.setStatusMessage("SERVICE FAULT");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			response.setResponse(e);
			response.setStatus(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION);
			response.setStatusMessage("SERVICE FAULT");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			response.setResponse(e);
			response.setStatus(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION);
			response.setStatusMessage("SERVICE FAULT");
			e.printStackTrace();
		} 
		catch( Exception e ) {
			response.setResponse(e);
			response.setStatus(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION);
			response.setStatusMessage("SERVICE FAULT");
		}
		
		return response;
	}
}
