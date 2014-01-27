package br.ufpe.cin.middleware.services.cloud.manager.monitor;

import java.util.List;
import java.util.ArrayList;
import java.util.Observable;
import br.ufpe.cin.middleware.naming.Service;
import br.ufpe.cin.middleware.naming.ServiceCreator;
import br.ufpe.cin.middleware.services.cloud.ManageInformation;
import br.ufpe.cin.middleware.distribution.stub.LocalAgentStub;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.ClientInvocationContext;

public class GlobalAgent extends Observable implements Runnable{
	
	private LocalAgentStub las	= null;
	
	public GlobalAgent(){
		
	}
	
	public GlobalAgent(Service service){
		this.setLocalAgentStub(service);
	}
	
	private void setLocalAgentStub(Service service) {
		try {
			this.las = (LocalAgentStub)new ServiceCreator(service).createStub();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
/*	public void setAgent(Service service) {
		this.agent = service;
	}*/

	public void run() {
		List<ManageInformation> mil	= new ArrayList<ManageInformation>();
		while(true){
			mil.add(new ManageInformation(this.las.getHost() + "." + this.las.getServiceName(),
					"CPUUsage", this.las.getCPUUsage().toString(),
					System.currentTimeMillis()));
			mil.add(new ManageInformation(this.las.getHost() + "." + this.las.getServiceName(),
					"MemUsage", this.las.getMemUsage().toString(),
					System.currentTimeMillis()));

			mil.add(new ManageInformation(this.las.getHost() + "." + this.las.getServiceName(),
					"RequestsPerSecondMetricForInvoker",
					this.las.getRequestsPerSecondMetricForInvoker().toString(),
					System.currentTimeMillis()));

			mil.add(new ManageInformation(this.las.getHost() + "." + this.las.getServiceName(),
					"ResponseTimeMetricForInvoker",
					this.las.getResponseTimeMetricForInvoker().toString(),
					System.currentTimeMillis()));

			mil.add(new ManageInformation(this.las.getHost() + "." + this.las.getServiceName(),
					"ResponseTimeMetricForInvoker",
					this.las.getRequestsPerSecondMetricForRemoteObject(
							new ClientInvocationContext("calculadora", "soma").toString()).toString(),
							System.currentTimeMillis()));
			this.setChanged();
			this.notifyObservers(mil);
		}
	}
}		
	
