package br.ufpe.cin.middleware.facade;

public class Manager {

	private MiddlewareConfig config;
	public static Manager INSTANCE;
	
	public Manager(MiddlewareConfig config) {
		this.setConfig(config);
	}

	public synchronized static Manager createInstance(MiddlewareConfig config){
		if(INSTANCE == null){
			INSTANCE = new Manager(config);
		}
		return INSTANCE;
	}
	
	public static Manager getInstance(){
		return INSTANCE;
	}

	private void setConfig(MiddlewareConfig config) {
		this.config = config;
	}

	public MiddlewareConfig getConfig() {
		return config;
	}
}
