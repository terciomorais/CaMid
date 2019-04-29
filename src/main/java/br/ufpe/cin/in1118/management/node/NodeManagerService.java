package br.ufpe.cin.in1118.management.node;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.stub.INodeManagerService;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.protocols.communication.Parameter;
import br.ufpe.cin.in1118.services.commons.naming.LocalServiceRegistry;
import br.ufpe.cin.in1118.utils.Network;

public class NodeManagerService implements Runnable, Serializable, INodeManagerService {

	private static final long serialVersionUID = -6276611610602776099L;
	private Message receivedMessage = null;

	public NodeManagerService() {
	}

	public NodeManagerService(Message message) {
		this.receivedMessage = message;
	}

	@Override
	public void addService(String serviceName, String className) {
		boolean feEnable = false;
		String feHost = "localhost";
		int fePort = 1313;
		String serverHost = "localhost";
		int serverPort = Integer.parseInt((String) Broker.getSystemProps().getProperties().get("port_number"));
		try {
			LocalServiceRegistry.getINSTANCE().addService(serviceName, Class.forName(className));
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (Broker.getSystemProps().getProperties().containsKey("frontend")
				&& Broker.getSystemProps().getProperties().get("frontend").equals("enabled")) {

			feHost = Network.recoverAddress((String) Broker.getSystemProps().getProperties().get("frontend_host"));
			fePort = Integer.parseInt((String) Broker.getSystemProps().getProperties().get("frontend_port"));
		}

		try {
			Broker.publishService(serviceName, Broker.getStub(className), feEnable, feHost, fePort, serverHost, serverPort);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Map<String, List<InvokingDataPoint>> getServiceData() {
		if (NodeManager.getInstance().isObjectMonitorEnabled())
			return NodeManager.getInstance().getObjectMonitor().getTimeSeries();
		else
			return null;
	}

	public SystemDataPoint getSystemData(){
		if (NodeManager.getInstance().isSysMonitorEnabled())
			return NodeManager.getInstance().getSystemMonitor().getLastSystemDataPoint();
		else
			return null;
	}
	
	@Override
	public void run() {
		String operation	= this.receivedMessage.getBody().getRequestHeader().getOperation();
		Parameter[] params	= this.receivedMessage.getBody().getRequestBody().getParameters();
		if (operation.equals("addService")){
			this.addService(((String) params[0].getValue()).toLowerCase(), (String)params[1].getValue());
		} /* else if(operation.equals("getMonitorData")){
			
		} */
	}
}
