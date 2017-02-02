package br.ufpe.cin.in1118.management.node;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.services.commons.naming.LocalServiceRegistry;
import br.ufpe.cin.in1118.utils.Network;

public class NodeManagerService {

	public void addService(String serviceName, String className){
		boolean feEnable = false;
		String feHost = "localhost";
		int fePort = 1313;
		String serverHost = "localhost";
		int serverPort =
				Integer.parseInt((String)Broker.getSystemProps().getProperties().get("port_number"));
		try {
			Class<?> clazz = Class.forName(className);
			LocalServiceRegistry.getINSTANCE().addService(serviceName, clazz);

			if(Broker.getSystemProps().getProperties().containsKey("frontend")){
				String frontend = (String) Broker.getSystemProps().getProperties().get("frontend");
				feEnable		= frontend.equals("enabled") ? true : false;
				feHost			= Network.recoverAddress((String)
									Broker.getSystemProps().getProperties().get("fe_host"));
				fePort			= Integer.parseInt((String)
									Broker.getSystemProps().getProperties().get("frontend_port"));
			}
			
			Broker.publishService(serviceName, clazz, feEnable, feHost, fePort, serverHost, serverPort);
			
		} catch (ClassNotFoundException e) {
			System.out.println("[NodeManagerService] Class not found locally: " + className);
			e.printStackTrace();
		}




	}

}
