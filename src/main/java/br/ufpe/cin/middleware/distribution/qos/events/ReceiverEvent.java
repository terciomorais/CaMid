package br.ufpe.cin.middleware.distribution.qos.events;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;

public class ReceiverEvent 
{
	private long startTime;
	
	private long endTime;
	
	private MethodRequestMessage requestMessage;
	
	private MethodResponseMessage responseMessage;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public MethodRequestMessage getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(MethodRequestMessage requestMessage) {
		this.requestMessage = requestMessage;
	}

	public MethodResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(MethodResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
}
