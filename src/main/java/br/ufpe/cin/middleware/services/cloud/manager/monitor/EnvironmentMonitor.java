package br.ufpe.cin.middleware.services.cloud.manager.monitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import br.ufpe.cin.middleware.facade.Manager;
import br.ufpe.cin.middleware.facade.Middleware;
import br.ufpe.cin.middleware.facade.MiddlewareConfig;
import br.ufpe.cin.middleware.naming.Naming;
import br.ufpe.cin.middleware.naming.Service;
import br.ufpe.cin.middleware.services.cloud.ManageInformation;

public class EnvironmentMonitor implements Observer{
	
	private List<GlobalAgent> globalAgList 			= new ArrayList<GlobalAgent>();
	private List<ManageInformation> appInformations	= null;
	private Naming naming								= null;
	private String target								= "LocalAgent";
	private int monitoringInterval						= 100;
	private int refreshInterval						= 100;
	
	public EnvironmentMonitor(String tg){
		MiddlewareConfig config = Middleware.getInstance().getConfig();
		this.naming = new Naming(config.getProperty(MiddlewareConfig.NAMING_SERVICE_HOST).toString(),
				Integer.parseInt(config.getProperty(MiddlewareConfig.NAMING_SERVICE_PORT).toString()));
		this.target = tg;
		this.updateServices();
	}
	public EnvironmentMonitor(){
		MiddlewareConfig config = Manager.getInstance().getConfig();
		this.naming = new Naming(config.getProperty(MiddlewareConfig.NAMING_SERVICE_HOST).toString(),
				Integer.parseInt(config.getProperty(MiddlewareConfig.NAMING_SERVICE_PORT).toString()));
		this.updateServices();
	}
		
	public void start(){
		Iterator<GlobalAgent> it = this.globalAgList.iterator();
		System.out.println("LocalAgent size " + this.globalAgList.size());
		while(it.hasNext()){
			new Thread(it.next()).start();
		}
	}
	
	public void updateServices(){
		try {
			List<Service> las;
			las = this.naming.lookup(this.target);
			for(Service s : las){
				GlobalAgent ga = new GlobalAgent(s);
				ga.addObserver(this);
				this.globalAgList.add(ga);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error at EnvironmentMonitor - Try to lookup services.");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		this.setAppInformations((List<ManageInformation>)arg);
		for(ManageInformation mi : this.appInformations)
			System.out.println("[EnvironmentMonitor]	" + mi.getUId() + " = " + mi.getValue());
	}

	private void setAppInformations(List<ManageInformation> args) {
		this.appInformations = args;
	}
	
	public List<ManageInformation> getAppinformations(){
		return this.appInformations;
	}
	
	public List<GlobalAgent> getGlobalAgList() {
		return globalAgList;
	}

	public int getMonitoringInterval() {
		return monitoringInterval;
	}

	public void setMonitoringInterval(int monitoringInterval) {
		this.monitoringInterval = monitoringInterval;
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
}