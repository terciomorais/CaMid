package br.ufpe.cin.middleware.distribution.stub;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;
import br.ufpe.cin.middleware.distribution.protocol.RequestParameter;
import br.ufpe.cin.middleware.distribution.remote.Delay;

public class DelayStub extends AbstractStub implements Stub
{

	public long delay(long timeInMilis)
	{
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("delay");
		outputMessage.getParameters().add(new RequestParameter("timeInMilis", timeInMilis));

		MethodResponseMessage incomingMessage = sendReceive(outputMessage);

		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException(incomingMessage.getStatusMessage());
		}
		return (Long)incomingMessage.getResponse();
	}
	
	@Override
	public String getServiceName() 
	{
		return Delay.SERVICE_NAME;
	}
	
}
