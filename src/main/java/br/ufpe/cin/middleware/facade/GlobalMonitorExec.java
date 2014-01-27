package br.ufpe.cin.middleware.facade;

import br.ufpe.cin.middleware.services.cloud.manager.monitor.EnvironmentMonitor;
import br.ufpe.cin.middleware.services.cloud.manager.monitor.IaaSMonitor;


public class GlobalMonitorExec 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		MiddlewareConfig config = new MiddlewareConfig();
		config.readFromURL(ClassLoader.getSystemResource("middleware.properties"));
		Manager.createInstance(config);
		//Middleware m = Middleware.createInstance(config);
		//m.initializeExecutors();

		IaaSMonitor im = new IaaSMonitor();
		im.start();
		EnvironmentMonitor em = new EnvironmentMonitor();
		em.start();
	}
	
}
