package br.ufpe.cin.middleware.distribution.cloud;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;
import br.ufpe.cin.middleware.infrastructure.client.Sender;
import br.ufpe.cin.middleware.naming.Naming;
import br.ufpe.cin.middleware.naming.Service;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class CloudProxyTask implements Runnable{

	private Socket socket							= null;
	private List<Service> endpointService			= null;
	private Naming naming							= null;
	private MethodRequestMessage receivedMessage	= null;
	private MethodResponseMessage responseMessage	= null;
	private PropertiesSetup properties				= null;
	
	public CloudProxyTask(Socket socket, PropertiesSetup props) {
		this.setSocket(socket);
		this.setProperties(props);
	}

	public List<Service> getEndpointService() {
		return this.endpointService;
	}

	public void setEndpointService(List<Service> list) {
		this.endpointService = list;
	}

	public Naming getNaming() {
		return naming;
	}

	public void setNaming(Naming naming) {
		this.naming = naming;
	}

	public MethodResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(MethodResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public MethodRequestMessage getReceivedMessage() {
		return receivedMessage;
	}

	public void setReceivedMessage(MethodRequestMessage receivedMessage) {
		this.receivedMessage = receivedMessage;
	}

	public PropertiesSetup getProperties() {
		return properties;
	}

	public void setProperties(PropertiesSetup properties) {
		this.properties = properties;
	}

	private void setSocket(Socket socket) {
		this.socket = socket;
		
	}

	@Override
	public void run() {
		//getting and processing the client request message
		try {
			ObjectInputStream incomingMessageStream = new ObjectInputStream(this.socket.getInputStream());
			Object readObject = incomingMessageStream.readObject();
			this.receivedMessage = (MethodRequestMessage) readObject;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//seeking the service in the NS and redirecting the Request 
		String hostNS	= this.getProperties().getProperties().getProperty("naming_endpoint");
		int portNS		= Integer.parseInt(this.getProperties().getProperties().getProperty("naming_port"));
		
		try {
			this.naming = new Naming(hostNS, portNS);
			List<Service> endpointList = this.naming.lookup(this.receivedMessage.getServiceName());
			this.setEndpointService(endpointList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*Redirecting the request to Remote Service
		 *  Conditions must be checked
		 *  	There is only one endpoint: redirects the service
		 *  	There are more than one endpoint: the load balancing decides which one will send the request to
		 *  	No endpoint: deal the error 
		 */
		if ((this.getEndpointService() != null) || (!this.getEndpointService().isEmpty())){
			if(this.getEndpointService().size() == 1){
				String host = ((Service)endpointService.get(0)).getHost();
				int port	= (((Service)endpointService.get(0)).getPort());
				this.setResponseMessage(this.redirectRequest(host, port));
				try {
					ObjectOutputStream responseMsg = new ObjectOutputStream(this.socket.getOutputStream());
					responseMsg.writeObject(this.getResponseMessage());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
		
			} else if(this.getEndpointService().size() > 1){
			//TODO: call load balancing service to select the best endpoint 	
			} 
		}else{
			//TODO: handling the error
			System.out.println("Error: no service found in the Cloud naming Registry");
		}
	}
	
	//The redirectRequest method uses Sender from communication layer for connecting to server
	private MethodResponseMessage redirectRequest(String endpoint, int port) {
		MethodResponseMessage msg = null;
		
		Sender sender = new Sender(endpoint, port);
		msg = sender.sendReceive(this.receivedMessage);
		return msg;
	}
}
