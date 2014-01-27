package br.ufpe.cin.middleware.distribution.stub;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;
import br.ufpe.cin.middleware.distribution.protocol.RequestParameter;


public class LocalAgentStub extends AbstractStub implements Stub{


	public Double getResponseTimeMetricForRemoteObject(
			String invocationContext) 
	{
//		JSONObject methodInvocation = new JSONObject();
//		try {
//			methodInvocation.put("remoteClass", this.getServiceName());
//			methodInvocation.put("method", "getResponseTimeMetricForRemoteObject");
//			
//			JSONObject parameters = new JSONObject();
//			parameters.put("invocationContext", invocationContext);
//			
//			methodInvocation.put("parameters", parameters);
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("getResponseTimeMetricForRemoteObject");
		outputMessage.getParameters().add(new RequestParameter("invocationContext", invocationContext));

		MethodResponseMessage incomingMessage = sendReceive(outputMessage);
		
		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException((Exception)incomingMessage.getResponse());
		}

		return (Double)incomingMessage.getResponse();
	}

	public Double getResponseTimeMetricForInvoker() 
	{
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("getResponseTimeMetricForInvoker");
				
		MethodResponseMessage incomingMessage = sendReceive(outputMessage);
		
		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException((Exception)incomingMessage.getResponse());
		}

		return (Double)incomingMessage.getResponse();
	}

	public Double getRequestsPerSecondMetricForRemoteObject(
			String invocationContext) {
		
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("getRequestsPerSecondMetricForRemoteObject");
		
		outputMessage.getParameters()
			.add(new RequestParameter("invocationContext", invocationContext));

		MethodResponseMessage incomingMessage = sendReceive(outputMessage);
		
		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException((Exception)incomingMessage.getResponse());
		}

		return (Double)incomingMessage.getResponse();
	}

	public Double getRequestsPerSecondMetricForInvoker() 
	{
		
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("getRequestsPerSecondMetricForInvoker");
				
		MethodResponseMessage incomingMessage = sendReceive(outputMessage);
		
		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException((Exception)incomingMessage.getResponse());
		}

		return (Double)incomingMessage.getResponse();
	}
	
	public Double getFailureRatioForRemoteObject(String invocationContext) 
	{
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("getFailureRatioForRemoteObject");
		
		outputMessage.getParameters()
			.add(new RequestParameter("invocationContext", invocationContext));

		MethodResponseMessage incomingMessage = sendReceive(outputMessage);
		
		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException((Exception)incomingMessage.getResponse());
		}

		return (Double)incomingMessage.getResponse();
	}
	
	public Double getFailureRatioForInvoker() 
	{
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("getFailureRatioForInvoker");
				
		MethodResponseMessage incomingMessage = sendReceive(outputMessage);
		
		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException((Exception)incomingMessage.getResponse());
		}

		return (Double)incomingMessage.getResponse();
	}
	
	public Double getCPUUsage() 
	{
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("getCPUUsage");
				
		MethodResponseMessage incomingMessage = sendReceive(outputMessage);
		
		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException((Exception)incomingMessage.getResponse());
		}

		return (Double)incomingMessage.getResponse();
	}
	
	public Double getMemUsage() 
	{
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("getMemUsage");
				
		MethodResponseMessage incomingMessage = sendReceive(outputMessage);

		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException((Exception)incomingMessage.getResponse());
		}
		
		return (Double)incomingMessage.getResponse();
	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return "LocalAgent";
	}

}
