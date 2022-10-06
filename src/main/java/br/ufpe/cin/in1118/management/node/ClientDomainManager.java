package br.ufpe.cin.in1118.management.node;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.stub.DomainManagerStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;

public class ClientDomainManager implements Runnable {

	private String action;
	private String scaleLevel;
//	private  domainManager;

	public ClientDomainManager() {
	}

	public ClientDomainManager(String action, String scaleLevel){
		this.action			= action;
		this.scaleLevel		= scaleLevel;

	}

	@Override
	public void run() {
		boolean result = false;
		String	host = Broker.getSystemProps().getProperties().getProperty("naming_host");
		int 	port = Integer.parseInt(Broker.getSystemProps().getProperties().getProperty("naming_port"));
		if(this.action.equals("scaleout")){
			NamingStub naming = new NamingStub(host, port);
			DomainManagerStub domainManager	= (DomainManagerStub)naming.lookup("management");
			long ini = System.currentTimeMillis();
			result = domainManager.scaleOut(this.scaleLevel);
			System.out.println("[ClientDomainManager:32] Spent time for VM deployment: "
					+ ((System.currentTimeMillis() - ini)/1000) + " s");
		} else
			System.out.println("[ClientDomainManager:34] - Another function to be implemented");

		if (result){
			NodeManager.getInstance().getAnalyser().setPaused(!result);
			System.out.println("[ClientDomainManager:39] Returning to monitor");
		}
	}
}