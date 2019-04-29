package br.ufpe.cin.in1118.management.node;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.management.analysing.Analyser;
import br.ufpe.cin.in1118.management.node.monitoring.ObjectMonitor;
import br.ufpe.cin.in1118.management.node.monitoring.SystemMonitor;

public class NodeManager {
	private ObjectMonitor	objectMonitor			= null;
	private SystemMonitor	systemMonitor			= null;
	private Analyser		objectAnalyser			= new Analyser();
	private Analyser		systemAnalyser			= new Analyser("cpu");
	private boolean			sysMonitorEnabled		= false;
	private boolean			objectMonitorEnabled	= false;

	private static NodeManager INSTANCE = null;

	protected NodeManager() {
	}

	public static NodeManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new NodeManager();
		return INSTANCE;
	}

	public ObjectMonitor getObjectMonitor() {
		return this.objectMonitor;
	}

	private void setObjectMonitor(ObjectMonitor objectMonitor) {
		this.objectMonitor = objectMonitor;
	}

	public SystemMonitor getSystemMonitor() {
		return systemMonitor;
	}

	public void setSystemMonitor(SystemMonitor systemMonitor) {
		this.systemMonitor = systemMonitor;
	}

	public boolean isSysMonitorEnabled() {
		return sysMonitorEnabled;
	}

	public void setSysMonitorEnabled(boolean sysMonitorEnable) {
		this.sysMonitorEnabled = sysMonitorEnable;
		if (this.getSystemMonitor() == null && this.isSysMonitorEnabled()) {
			long interval = Long.parseLong((String) Broker.getSystemProps().getProperties().get("system_interval"));
			this.setSystemMonitor(new SystemMonitor(interval));
		}
	}

	public boolean isObjectMonitorEnabled() {
		return objectMonitorEnabled;
	}

	public void setObjectMonitorEnabled(boolean objectMonitorEnabled) {
		this.objectMonitorEnabled = objectMonitorEnabled;
		if (this.getObjectMonitor() == null && this.isObjectMonitorEnabled())
			this.setObjectMonitor(new ObjectMonitor());
	}

	public Analyser getObjectAnalyser() {
		return this.objectAnalyser;
	}
	
	public Analyser getSystemAnalyser() {
		return this.systemAnalyser;
	}
	//TODO Change communication to messaging
	public void alert(short alert){
		if(Broker.getSystemProps().getProperties().containsKey("dm_host") && Broker.getSystemProps().getProperties().containsKey("dm_port")){
/* 			this.analyser.setPaused(true);
			ClientCloudManager cdm = new ClientCloudManager("scale", alert);
			ClientDomainManager cdm = new ClientDomainManager("scaleup", "app");
			Broker.getExecutorInstance().execute(cdm); */
		} else 
			System.out.println("[NodeManager:73] OVERLOAD ALERT WITHOUT ACTION");
	}
	
	public void start() {
		if(this.isSysMonitorEnabled())
			Broker.getExecutorInstance().execute(this.getSystemMonitor());
		
		if(this.isObjectMonitorEnabled())
			Broker.getExecutorInstance().execute(this.getObjectMonitor());
	}
}
