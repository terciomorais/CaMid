package br.ufpe.cin.in1118.management.node;

import java.io.Serializable;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.protocols.communication.Parameter;
import br.ufpe.cin.in1118.services.commons.naming.LocalServiceRegistry;
import br.ufpe.cin.in1118.utils.Network;

public class NodeManagerService implements Runnable, Serializable{

	private static final long serialVersionUID = -6276611610602776099L;
	private Message receivedMessage = null;
	
	public NodeManagerService(){}
	
	public NodeManagerService(Message message){
		this.receivedMessage = message;
	}
	
	public void addService(String serviceName, Class<?> className){
		boolean	feEnable	= false;
		String	feHost		= "localhost";
		int		fePort		= 1313;
		String	serverHost	= "localhost";
		int		serverPort	=
				Integer.parseInt((String)Broker.getSystemProps().getProperties().get("port_number"));
		//Class<?> clazz = Class.forName(className);
		LocalServiceRegistry.getINSTANCE().addService(serviceName, className);

		if(Broker.getSystemProps().getProperties().containsKey("frontend")){
			String frontend = (String) Broker.getSystemProps().getProperties().get("frontend");
			feEnable		= frontend.equals("enabled") ? true : false;
			feHost			= Network.recoverAddress((String)
								Broker.getSystemProps().getProperties().get("frontend_host"));
			fePort			= Integer.parseInt((String)
								Broker.getSystemProps().getProperties().get("frontend_port"));
		}
		
		try {
			Class<?> clazz = Broker.getStub(className.getSimpleName());
			Broker.publishService(serviceName, clazz, feEnable, feHost, fePort, serverHost, serverPort);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String operation	= this.receivedMessage.getBody().getRequestHeader().getOperation();
		Parameter[] params	= this.receivedMessage.getBody().getRequestBody().getParameters();
		if (operation.equals("addService")){
			String param0	= (String) params[0].getValue();
			Class<?> param1	= (Class<?>) params[1].getValue();
			this.addService(param0, param1);
		}
	}

}
