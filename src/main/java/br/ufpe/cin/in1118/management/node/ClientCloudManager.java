package br.ufpe.cin.in1118.management.node;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.distribution.stub.CloudManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;

public class ClientCloudManager implements Runnable {

	private String action;
	private String scaleLevel;
	private short alertType;
	private Analysis analysis;
	// private domainManager;

	public ClientCloudManager() {
	}

	public ClientCloudManager(String action, short alert) {
		this.alertType = alert;
		this.action = action;
	}

	public ClientCloudManager(String action, String scaleLevel) {
		this.action			= action;
		this.scaleLevel		= scaleLevel;
	}

	public ClientCloudManager(Analysis analysis){
		this.analysis = analysis;
		
		this.action = "alert";
	}

	@Override
	public void run() {
		//boolean result = false;
		String	host = Broker.getSystemProps().getProperties().getProperty("naming_host");
		int 	port = Integer.parseInt(Broker.getSystemProps().getProperties().getProperty("naming_port"));
		
		NamingStub naming = new NamingStub(host, port);
		CloudManagerServiceStub domainManager = (CloudManagerServiceStub)naming.lookup("management");

		if(this.action.equals("alert")){
			System.out.println("[ClientCloudManager:45] Analysis data: " + analysis.getAlertMessage()
					+ "\n                     CPU usage: " + analysis.getSystemDataPoint().getCpuUsage().getAverage()
					+ "\n                     Service size: " + analysis.getSystemDataPoint().getSystemDataList().size());
			domainManager.alert(this.analysis);
		}
			

/* 		if(this.action.equals("scaleout")){
			result = domainManager.scaleOut(this.scaleLevel);
		} else if(this.action.equals("scale")){
			result = domainManager.scale(this.action, this.alertType);
		}
		else
			System.out.println("[ClientDomainManager:34] - Another function to be implemented");

		if (result){
			NodeManager.getInstance().getObjectAnalyser().setPaused(!result);
		} */
	}
}
