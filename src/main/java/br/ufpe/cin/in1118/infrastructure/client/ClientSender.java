package br.ufpe.cin.in1118.infrastructure.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import br.ufpe.cin.in1118.protocols.communication.Message;

public class ClientSender implements Callable<Message> {

	private ObjectOutputStream	oos				= null;
	private ObjectInputStream	ois				= null;
	private Socket				conn			= null;

	private String				host			= "localhost";
	private int					port			= 1313;

	private Message				receivedMessage	= null;
	private Message				toSendMessage	= null;

	public ClientSender(String host, int port, Message message) {
		this.toSendMessage = message;
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	@Override
	public Message call() {
		try {
			conn = new Socket(this.host, this.port);
			this.oos = new ObjectOutputStream(new BufferedOutputStream(conn.getOutputStream()));
			this.oos.writeObject(this.toSendMessage);
			this.oos.flush();
		} catch (UnknownHostException e) {
			System.err.println("[ClientSender:42] Error: Unknown Host Exception (message no. "
					+ this.receivedMessage.getUniqueID() + ") " + e.getMessage());
			this.receivedMessage = new Message();
			this.receivedMessage.setStatus(Message.ResponseStatus.SENDING_EXCEPTION);
			this.receivedMessage.setStatusMessage("Remote host not found by ClientSender");
			return this.receivedMessage;
		} catch (IOException e) {
			System.err.println("[ClientSender:48] Error during sending: I/O Exception host " + this.host
					+ " (message no. " + e.getMessage() + ")");
			//e.printStackTrace();
			this.receivedMessage = new Message();
			this.receivedMessage.setStatus(Message.ResponseStatus.SENDING_EXCEPTION);
			if (e.getMessage().contains("No route to host")){
				this.receivedMessage.setStatusMessage("No route to target host from ClientSender");
				System.err.println("[ClientSender:48] Error during sending: No route to target host from ClientSender " + this.host
					+ " (message no. " + e.getMessage() + ")");
			} else if(e.getMessage().contains("Connection refused")){
				this.receivedMessage.setStatusMessage("Connection to target host refused");
				System.err.println("[ClientSender:48] Error during sending: Connection to target host refused " + this.host
					+ " (message no. " + e.getMessage() + ")");
			} else {
				this.receivedMessage.setStatusMessage("I/O error on ClientSender");
				System.err.println("[ClientSender:48] Error during sending: I/O Exception host " + this.host
					+ " (message no. " + e.getMessage() + ")");
			}
			return this.receivedMessage;
		}
		
		try {
			
			this.ois = new ObjectInputStream(new BufferedInputStream(conn.getInputStream()));
			this.receivedMessage = (Message)this.ois.readObject();
		} catch (IOException e) {
			System.err.println("[ClientSender:62] Error during receiving: I/O Exception (message no. "
					+ e.getMessage());
			this.receivedMessage = new Message();
			this.receivedMessage.setStatus(Message.ResponseStatus.RECEPTION_EXCEPTION);
			this.receivedMessage.setStatusMessage("I/O error on ClientSender");			
			return this.receivedMessage;
		} catch (ClassNotFoundException e) {
			System.err.println("[ClientSender:67] Error: Class Not Found Exception (message no. "
					+ this.receivedMessage.getUniqueID()+ ") "
					+ e.getMessage());
			this.receivedMessage = new Message();
			this.receivedMessage.setStatus(Message.ResponseStatus.RECEPTION_EXCEPTION);
			this.receivedMessage.setStatusMessage("Class not found by ClientSender");
			return this.receivedMessage;
		} finally {
			try{
				if(this.ois != null)
					this.ois.close();
				if(this.oos != null)
					this.oos.close();
				if(!this.conn.isClosed())
					this.conn.close();
			} catch(NullPointerException | IOException ioe){
				System.err.println("[ClientSender:71] Error: Trying to close lost connection (message no. "
						+ this.receivedMessage.getUniqueID()+ ") "
						+ ioe.getMessage());
			} 
		}
		return this.receivedMessage;
	}
}
