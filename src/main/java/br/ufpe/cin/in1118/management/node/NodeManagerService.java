package br.ufpe.cin.in1118.management.node;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.stub.INodeManagerService;
import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.protocols.communication.Parameter;
import br.ufpe.cin.in1118.services.commons.naming.LocalServiceRegistry;
import br.ufpe.cin.in1118.utils.EndPoint;
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
		boolean	feEnable	= false;
		String	feHost		= Network.recoverAddress("localhost");
		int		fePort		= 1313;
		String	serverHost	= Network.recoverAddress("localhost");
		int		serverPort	= Integer.parseInt((String) Broker.getSystemProps().getProperties().get("port_number"));
		
		try {
			LocalServiceRegistry.getINSTANCE().addService(serviceName, Class.forName(className));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		if (Broker.getSystemProps().getProperties().containsKey("frontend")
				&& Broker.getSystemProps().getProperties().get("frontend").equals("enabled")) {

			feHost = Network.recoverAddress((String) Broker.getSystemProps().getProperties().get("frontend_host"));
			fePort = Integer.parseInt((String) Broker.getSystemProps().getProperties().get("frontend_port"));
		}

		System.out.println("\n[NodeManagerService:51] Adding service " + serviceName + " and the class " + className + "\n");
		try {
			Broker.publishService(serviceName, Broker.getStub(serviceName), feEnable, feHost, fePort, serverHost, serverPort);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addService(String serviceName, Stub stub) {
		
		stub.setHost(Network.recoverAddress("localhost"));
		stub.setPort(Integer.parseInt((String) Broker.getSystemProps().getProperties().get("port_number")));
		stub.setObjectId(1);

		try {
			Broker.getRegistry().addService(serviceName, Class.forName(stub.getClassName()));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		if (Broker.getSystemProps().getProperties().containsKey("frontend")
				&& Broker.getSystemProps().getProperties().get("frontend").equals("enabled")) {
			stub.setFeHost(Network.recoverAddress((String) Broker.getSystemProps().getProperties().get("frontend_host")));
			stub.setFePort(Integer.parseInt((String) Broker.getSystemProps().getProperties().get("frontend_port")));
			stub.setForwarded(true);
		}

		System.out.println("\n[NodeManagerService:80] Adding service " + serviceName + " and the class " + stub.getClassName() + "\n");
		
		Broker.getNaming().bind(serviceName, stub);
	}

	@Override
	public void removeService(String serviceName) {
		System.out.println("[NodemanagerService:88] Removing service from LocalRegistry " + serviceName);
		Broker.getRegistry().removeService(serviceName);
		EndPoint ep = new EndPoint(Network.recoverAddress("localhost"), Integer.parseInt((String) Broker.getSystemProps().getProperties().get("port_number")));
		Broker.getNaming().unbind(serviceName, ep);
	}

	public Map<String, List<InvokingDataPoint>> getServiceData() {
		if (NodeManager.getInstance().isObjectMonitorEnabled())
			return NodeManager.getInstance().getObjectMonitor().getTimeSeries();
		else
			return null;
	}

	public SystemDataPoint getSystemData() {
		if (NodeManager.getInstance().isSysMonitorEnabled())
			return NodeManager.getInstance().getSystemMonitor().getLastSystemDataPoint();
		else
			return null;
	}

	@Override
	public void run() {
		String operation = this.receivedMessage.getBody().getRequestHeader().getOperation();
		Parameter[] params = this.receivedMessage.getBody().getRequestBody().getParameters();
		if (operation.equals("addService")) {
			this.addService(((String) params[0].getValue()).toLowerCase(), (String) params[1].getValue());
		} /*
			 * else if(operation.equals("getMonitorData")){
			 * 
			 * }
			 */
	}
}
