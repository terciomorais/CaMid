package br.ufpe.cin.in1118.services.commons.naming;

import java.util.Map;

import br.ufpe.cin.in1118.distribution.stub.CloudManagerServiceStub;
import br.ufpe.cin.in1118.utils.EndPoint;

public class DispatcherThread implements Runnable {
	private CloudManagerServiceStub	cloudManager	= null;
	private String 					serviceName 	= null;
	private Map<String, NameRecord> repository		= null;
	private String 					operation		= null;
	private EndPoint				endpoint		= null;
	
	public DispatcherThread(CloudManagerServiceStub domainManager, String serviceName, Map<String, NameRecord> repository, String operation, EndPoint endpoint){
		this.cloudManager	= domainManager;
		this.serviceName	= serviceName;
		this.repository		= repository;
		this.operation		= operation;
		this.endpoint		= endpoint;
	}

	@Override
	public void run() {
		if (this.operation.toLowerCase().equals("addservice"))
			this.cloudManager.addService(serviceName, repository.get(this.serviceName));
		else if (this.operation.toLowerCase().equals("removeservice"))
			this.cloudManager.removeService(serviceName);
		else if (this.operation.toLowerCase().equals("removeendpoint"))
			this.cloudManager.removeServiceEndpoint(serviceName, endpoint);
	}
}