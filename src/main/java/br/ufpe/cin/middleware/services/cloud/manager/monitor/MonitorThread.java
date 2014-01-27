package br.ufpe.cin.middleware.services.cloud.manager.monitor;

import java.util.Observable;
import br.ufpe.cin.middleware.services.cloud.RawInformation;

public class MonitorThread extends Observable implements Runnable{
	protected IaaSInfoRequestor iaaSRequestor	= null;
	protected RawInformation rawInformation	= null;
	private int interval						= 100; //interval of monitoring time in ms
	private String sourceType					= null;
	
	public MonitorThread(String sourceType){
		this.setSourceType(sourceType);
		this.setIaaSRequestor();
	}
	
	public MonitorThread(String sourceType, int interval){
		this.setInterval(interval);
		this.setSourceType(sourceType);
		this.setIaaSRequestor();
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;	
	}
	public String getSourceType(){
		return this.sourceType;
	}

	public RawInformation getRawInformation() {
		return this.rawInformation;
	}

	public void setRawInformation(){
		this.rawInformation = new RawInformation(
				this.iaaSRequestor.getPoolInfo(this.sourceType),
				this.sourceType,
				this.iaaSRequestor.getTimeStamp());
	}
	
	private void updateRawInformation() {
		this.iaaSRequestor.getPoolInfo(this.sourceType);
		this.rawInformation.update(this.iaaSRequestor.getTimeStamp(),
				this.iaaSRequestor.getPoolInfo(this.sourceType));
	}
	
	public void setIaaSRequestor(){
		this.iaaSRequestor = new IaaSInfoRequestor(this.sourceType);
	}

	public IaaSInfoRequestor getIaaSRequestor(){
		return this.iaaSRequestor;
	}

	public int getInterval() {
		return this.interval;
	}

	public void setInterval(int time) {
		if (time > 0)
			this.interval = time;
	}
	
	public void gather(){
		this.setRawInformation();
	}

	public void run() {
		this.setRawInformation();
		while (true) {
			this.updateRawInformation();
//Notification about new gathering
			this.setChanged();
			this.notifyObservers(this.getRawInformation().getResources());

/*			System.out.println("[MonitorThread]:");
			for(Resource vmr : this.getRawInformation().getResources()){
				System.out.println("	. " + vmr.getUId() + " - " + vmr.getName() +
						"; tamanho: " + ((VMResource)vmr).getNetRX().size() +
						"; IP address: " + ((VMResource)vmr).getIpAddress());
				for(ManageInformation mi : ((VMResource)vmr).getNetRX())
					System.out.println("	." + mi.getUId() + " - " + mi.getValue());
		}*/
			try {
				Thread.sleep(this.getInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}