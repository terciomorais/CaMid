package br.ufpe.cin.middleware.infrastructure.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import br.ufpe.cin.middleware.distribution.Invoker;
import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;
import br.ufpe.cin.middleware.services.LocalServiceRegistry;

public class Receiver implements Runnable {
	
	private ObjectInputStream incomingMessageStream = null;
	private ObjectOutputStream outputMessageStream = null;
	private Socket conn;

	private MethodRequestMessage receivedMessage = null;
	private MethodResponseMessage dispatchedMessage = null;
	private Invoker invoker = null;
	private String fromHostID;
	private LocalServiceRegistry registry;
	
	public MethodRequestMessage getReceivedMessage() {
		return receivedMessage;
	}

	public void setReceivedMessage(MethodRequestMessage receivedMessage) {
		this.receivedMessage = receivedMessage;
	}

	public MethodResponseMessage getDispatchedMessage() {
		return dispatchedMessage;
	}

	public void setDispatchedMessage(MethodResponseMessage dispatchedMessage) {
		this.dispatchedMessage = dispatchedMessage;
	}

	public Receiver(Socket socket, LocalServiceRegistry registry){
		this.setConnection(socket);
		this.registry = registry;
	}
	
	public void setConnection(Socket socket) {
		this.conn = socket;
	}
	public void run(){
		try {
			
			this.incomingMessageStream = new ObjectInputStream(this.conn.getInputStream());
			Object read = this.incomingMessageStream.readObject();
			this.processMessage(read);
			System.out.println("Receiving message from " + this.receivedMessage.getSenderHost());
			
			this.invoker = new Invoker(this.receivedMessage, registry);
			
			this.outputMessageStream = new ObjectOutputStream(this.conn.getOutputStream());
			this.outputMessageStream.writeObject(this.dispatchedMessage);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally	{
			try {
				this.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void processMessage(Object object) {
		this.receivedMessage = (MethodRequestMessage)object;

		try {
			this.invoker = new Invoker(this.receivedMessage, registry);
			this.invoker.processMessage();
		} catch (Exception e) {
			System.err.println("ERRO na Classe invoker");
			e.printStackTrace();
		}
		this.setDispatchedMessage(this.invoker.getResponseMessage());
	}
	
	private void close() throws IOException{
		this.incomingMessageStream.close();
		this.outputMessageStream.close();
		this.conn.close();
	}

	public void setFromHostID(String fromHostID) {
		this.fromHostID = fromHostID;
	}

	public String getFromHostID() {
		return fromHostID;
	}
}