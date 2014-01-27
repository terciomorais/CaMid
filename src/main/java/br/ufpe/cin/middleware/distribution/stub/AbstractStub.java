package br.ufpe.cin.middleware.distribution.stub;

import br.ufpe.cin.middleware.distribution.Marshaller;
import br.ufpe.cin.middleware.distribution.ObjectDescriptor;
import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;
import br.ufpe.cin.middleware.infrastructure.client.Sender;
import br.ufpe.cin.middleware.infrastructure.client.SenderQueue;

public abstract class AbstractStub implements Stub{

//	private static ExecutorService executor = Executors.newCachedThreadPool();
	
//	protected MethodResponseMessage incomingMessage = null;
//	protected MethodRequestMessage outputMessage = null;
	
	
	protected String host;
	protected int port;
	protected Sender sender;
	protected Marshaller marshaller = null;
	ObjectDescriptor objDesc = null;

	protected SenderQueue senderQueue = SenderQueue.getInstance();
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public void setConnection(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void send(MethodRequestMessage message) {
//		this.sender = new Sender(this.host, this.port);
//		sender.send(message);
	}

	
	public MethodResponseMessage receive() {
		MethodResponseMessage msg = null;
//		try {
//			msg = (MethodResponseMessage) this.sender.receive();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		return msg;
	}
	
	public MethodResponseMessage sendReceive(MethodRequestMessage outputMessage)
	{
		MethodResponseMessage response = senderQueue.doSend(new Sender(this.host,this.port), outputMessage);	
		return response;
	}
	
	public void close() {
		
	}

	public abstract String getServiceName();
	
}
