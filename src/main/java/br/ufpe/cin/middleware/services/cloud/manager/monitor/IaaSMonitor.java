package br.ufpe.cin.middleware.services.cloud.manager.monitor;

import java.util.List;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Observable;
import br.ufpe.cin.middleware.services.cloud.VMResource;
import br.ufpe.cin.middleware.services.cloud.ManageInformation;

public class IaaSMonitor implements Observer {
//	private MonitorThread hostMonitor			= null; //new Monitor("HOST");
	private MonitorThread vmMonitor			= null; //new Monitor("VM");
	private List<MonitorThread> monitorList	= null;
//	private MonitorThread vnMonitor				= null; //new Monitor("VNET");
	private List<VMResource> vmResources		= null;
//	private List<Resource> hostResources		= null;
	
	public IaaSMonitor(){
		this.vmMonitor 	= new MonitorThread("VM");		
		this.vmMonitor.addObserver(this);
		this.vmMonitor.setInterval(1);
/*		this.hostMonitor = new MonitorThread("HOST");
		this.hostMonitor.addObserver(this);
		this.hostMonitor.setInterval(10);
*/	}
	
	public void start() {
		Thread tVM = new Thread(this.vmMonitor);
//		Thread tHost = new Thread(this.hostMonitor);
//		tHost.start();
		tVM.start();
		
		//this.setVMResources(this.vmMonitor.getRawInformation().getResources());
	}

	public List<VMResource> getVMResources(){
		return this.vmResources;
	}
	private void setVMResources(List<VMResource> resources){
		this.vmResources = (List<VMResource>) resources;
	}
	
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		if (arg instanceof List){
			this.setVMResources((ArrayList<VMResource>)arg);
			for(VMResource vmr : this.getVMResources()){
				System.out.println("[IaaSMonitor] Resource: " + vmr.getUId());
				for(ManageInformation mi : vmr.getInformations())
					System.out.println("	" + mi.getName() + " = " + mi.getValue());
			}
		}
	}
}