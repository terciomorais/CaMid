package br.ufpe.cin.in1118.infrastructure.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.server.Invoker;
import br.ufpe.cin.in1118.management.monitoring.Event;
import br.ufpe.cin.in1118.management.monitoring.EventSource;
import br.ufpe.cin.in1118.management.monitoring.EventSourceFactory;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.utils.Network;

public class Receiver implements Runnable{
	private Socket				conn 			= null;
	private ObjectOutputStream	outToClient		= null;
	private ObjectInputStream	inFromClient	= null;
	private Message				receivedMessage	= null;
	private Message				replyMessage	= null;
	private Invoker				invoker			= null;
	private EventSource			eventSource		= null;

	private boolean	isMonitored	= NodeManager.getInstance().isObjectMonitorEnabled();

	public Receiver(Socket conn){
		/*		initTime = System.currentTimeMillis();*/
		this.conn = conn;
		if (this.isMonitored){
			this.eventSource = EventSourceFactory.getInstance().createEventSource();
			this.eventSource.setEvent(new Event());
			this.eventSource.startTimer();
			this.eventSource.startForthTime();
		}
	}

	@Override
	public void run() {
		try {
			this.outToClient		= new ObjectOutputStream(new BufferedOutputStream(conn.getOutputStream()));
			this.inFromClient		= new ObjectInputStream(new BufferedInputStream(this.conn.getInputStream()));
			this.receivedMessage	= (Message) this.inFromClient.readObject();
			String magic			= this.receivedMessage.getHeader().getMagic();


/* 			if(!this.receivedMessage.getBody().getRequestHeader().getServiceName().equals("delay")){
				System.out.println("[Receiver:118] Received message: " + this.receivedMessage);
				System.out.println("[Receiver:119] Body of received message: " + this.receivedMessage.getBody());
				System.out.println("[Receiver:220] Request header of received message: " + this.receivedMessage.getBody().getRequestHeader());
				System.out.println("[Receiver:221] Service of received message: " + this.receivedMessage.getBody().getRequestHeader().getServiceName());
				System.out.println("[Receiver:222] Method of received message: " + this.receivedMessage.getBody().getRequestHeader().getOperation());
			} */
				Message msg = this.receivedMessage;
				this.invoker = new Invoker(msg);

				this.replyMessage = this.invoker.invoke();
				this.replyMessage.setUniqueID(this.receivedMessage.getUniqueID());
				this.replyMessage.addRouteTrack(Network.recoverAddress("localhost"));
				this.outToClient.writeObject(this.replyMessage);
				this.outToClient.flush();
		//	}

			//set up the event
			if (this.isMonitored){
				this.eventSource.stopTimer();
				this.eventSource.getEvent().setContext(magic);
				this.eventSource.getEvent().setSuccess(true);
				this.eventSource.getEvent().setService(this.receivedMessage.getBody().getRequestHeader().getServiceName());
				this.eventSource.getEvent().setSourceEndPoint(this.receivedMessage.getBody().getRequestHeader().getSourceEndPoint());
			}

		} catch (IOException ioe) {
			if(this.isMonitored){
				this.eventSource.getEvent().setException(ioe);
				this.eventSource.getEvent().setSuccess(false);
				this.eventSource.getEvent().setService(this.receivedMessage
						.getBody().getRequestHeader().getServiceName());
			}
			System.out.println("[Receiver:80] Error I/O exception (Message no. "
					+ this.replyMessage.getUniqueID()
					+ "; context: " + Broker.getROLE()
					+ "): " + ioe.getMessage());
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			//set up event
			if(this.isMonitored){
				this.eventSource.getEvent().setException(cnfe);
				this.eventSource.getEvent().setSuccess(false);
				this.eventSource.getEvent().setService(this.receivedMessage
						.getBody().getRequestHeader().getServiceName());
			}

			//Preparing and responding with error
			System.out.println("[Receiver:95] Error Class not found exception (Message no. "
				+ this.replyMessage.getUniqueID() + "): "
				+ cnfe.getMessage());
			cnfe.printStackTrace();
			this.replyMessage = new Message();
			this.replyMessage.setStatus(Message.ResponseStatus.RECEPTION_EXCEPTION);
			this.replyMessage.setStatusMessage("Class not found by Receiver.");
			this.replyMessage.setUniqueID(this.receivedMessage.getUniqueID());
			try {
				this.outToClient.writeObject(this.replyMessage);
				this.outToClient.flush();
			} catch (IOException e) {
				System.out.println("[Receiver:107] Error trying to respond to client");
			}
			/*			System.out.println("[Receiver:95] Processing time "
					+ (System.currentTimeMillis() - initTime));*/

		} finally {
			//set up event
			if(this.isMonitored){
				// this.eventSource.getEvent()
				// 	.setSourceEndPoint(conn.getLocalAddress()
				// 	.getHostAddress() + ":" + conn.getLocalPort());
				
				this.eventSource.notifyObservers();
				this.eventSource.getEvent().setService(this.receivedMessage.getBody().getRequestHeader().getServiceName());
			}

			try {
				this.inFromClient.close();
				this.outToClient.close();
				if(this.conn.isClosed())
					this.conn.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}	
		}
	}
}
