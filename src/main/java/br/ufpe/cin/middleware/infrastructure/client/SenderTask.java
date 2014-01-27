package br.ufpe.cin.middleware.infrastructure.client;

import java.util.concurrent.Callable;
import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;

public class SenderTask implements Callable<MethodResponseMessage>{

	private Sender sender;
	private MethodRequestMessage request;
	
	public SenderTask(Sender sender, MethodRequestMessage request){
		this.sender = sender;
		this.request = request;
	}
	
	@Override
	public MethodResponseMessage call() throws Exception {
		MethodResponseMessage response = null;
		response = this.sender.sendReceive(request);
		return response;
	}	
}
