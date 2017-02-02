package br.ufpe.cin.in1118.management.node;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.management.monitoring.Analyser;
import br.ufpe.cin.in1118.management.node.monitoring.ObjectMonitor;
import br.ufpe.cin.in1118.management.node.monitoring.SystemMonitor;

public class NodeManager {
	private ObjectMonitor	objectMonitor			= null;
	private SystemMonitor	systemMonitor			= null;
	private Analyser		analyser				= new Analyser();
	private boolean			sysMonitorEnabled		= false;
	private boolean			objectMonitorEnabled	= false;
	
	private static NodeManager	INSTANCE			= null;
	
	protected NodeManager (){}
	
	public static NodeManager getInstance(){
		if(INSTANCE == null)
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

	private boolean isSysMonitorEnabled() {
		return sysMonitorEnabled;
	}

	public void setSysMonitorEnabled(boolean sysMonitorEnable) {
		this.sysMonitorEnabled = sysMonitorEnable;
		if(this.getSystemMonitor() == null && this.isSysMonitorEnabled()){
			long interval = Long.parseLong((String) Broker.getSystemProps().getProperties().get("system_interval"));
			this.setSystemMonitor(new SystemMonitor(interval));
		}
	}

	public boolean isObjectMonitorEnable() {
		return objectMonitorEnabled;
	}

	public void setObjectMonitorEnable(boolean objectMonitorEnable) {
		this.objectMonitorEnabled = objectMonitorEnable;
		if(this.getObjectMonitor() == null && this.isObjectMonitorEnable())
			this.setObjectMonitor(new ObjectMonitor());
	}

	public Analyser getAnalyser(){
		return this.analyser;
	}
	
	public void alert(){
		if(Broker.getSystemProps().getProperties().containsKey("dm_host")
				&& Broker.getSystemProps().getProperties().containsKey("dm_port")){
			this.analyser.setPaused(true);
			this.scaleOut();
		} else 
			System.out.println("[NodeManager:73] OVERLOAD ALERT WITHOUT ACTION");
	}
	
	private void scaleOut() {
		ClientDomainManager cdm = new ClientDomainManager("scaleout", "vm");
		Broker.getExecutorInstance().execute(cdm);
	}

	public void start() {
		if(this.isSysMonitorEnabled())
			Broker.getExecutorInstance().execute(this.getSystemMonitor());
		
		if(this.isObjectMonitorEnable())
			Broker.getExecutorInstance().execute(this.getObjectMonitor());
	}
}
