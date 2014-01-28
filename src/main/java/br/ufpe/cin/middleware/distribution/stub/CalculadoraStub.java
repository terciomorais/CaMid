package br.ufpe.cin.middleware.distribution.stub;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;
import br.ufpe.cin.middleware.distribution.protocol.RequestParameter;

@XmlRootElement
public class CalculadoraStub extends AbstractStub implements Stub {
	
	final String  NAME = "Calculadora";

	public CalculadoraStub() {
	}
	
	public int soma(int a, int b) throws Exception{
		
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("soma");
		outputMessage.getParameters().add(new RequestParameter("a", a));
		outputMessage.getParameters().add(new RequestParameter("b", b));

		MethodResponseMessage incomingMessage = sendReceive(outputMessage);

		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))	{
			throw new RuntimeException(incomingMessage.getStatusMessage());
		}
		return (Integer)incomingMessage.getResponse();
	}
	
	public int sub(int a, int b) throws Exception{
		
		MethodRequestMessage outputMessage = null;
		
		outputMessage = new MethodRequestMessage();
		outputMessage.setServiceName(this.getServiceName());
		outputMessage.setMethod("sub");
		outputMessage.getParameters().add(new RequestParameter("a", a));
		outputMessage.getParameters().add(new RequestParameter("b", b));

		MethodResponseMessage incomingMessage = sendReceive(outputMessage);

		if(incomingMessage.getStatus().equals(MethodResponseMessage.ResponseStatus.SERVICE_EXCEPTION))
		{
			throw new RuntimeException(incomingMessage.getStatusMessage());
		}
		return (Integer)incomingMessage.getResponse();
	}

	@Override
	public String getServiceName() {
		return "Calculadora";
	}
}
