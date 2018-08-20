package br.ufpe.cin.middleware.app.statistics;

import br.ufpe.cin.camid.distribution.skeleton.Delay;
import br.ufpe.cin.camid.distribution.stub.DelayStub;
import br.ufpe.cin.camid.facade.Middleware;
import br.ufpe.cin.camid.facade.MiddlewareConfig;
import br.ufpe.cin.camid.service.naming.Service;
import br.ufpe.cin.camid.service.naming.ServiceCreator;

public class JMeterLoader {
	
	
	private String host;
	private int port;
	
	private MiddlewareConfig config;
	
	private Middleware m;

	private DelayStub delayService;
	
	public JMeterLoader(String host, int port)
	{
		this.host = host;
		this.port = port;
		
		config = new MiddlewareConfig();
		config.setProperty(MiddlewareConfig.SERVER_ENABLED, "false");
		config.setProperty(MiddlewareConfig.NAMING_ENABLED, "false");
		config.setProperty(MiddlewareConfig.NAMING_SERVICE_HOST, host);
		config.setProperty(MiddlewareConfig.NAMING_SERVICE_PORT, ""+port);
		m = Middleware.createInstance(config);
		
		Service endpoint = null;
		try {
			endpoint = (Service) m.getNaming().lookup(Delay.SERVICE_NAME).get(0);
//																		getLogger.debug("Service "+ Delay.SERVICE_NAME +" found. Creating client...");
			delayService = (DelayStub) new ServiceCreator(endpoint).createStub();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public DelayStub createStub()
	{
		return delayService;
	}

	
	public static void main(String[] args) {
		int initial = 150;
		int increment = 150;
		
		int iterations = 5;
		
		for(int i = 0; i < iterations; i++)
		{
			System.out.println(initial + increment*i);
		}
	}
}
