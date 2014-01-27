package br.ufpe.cin.middleware.facade;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.middleware.distribution.qos.events.InvokerEventSourceFactory;
import br.ufpe.cin.middleware.distribution.qos.events.RemoteObjectEventSourceFactory;
import br.ufpe.cin.middleware.distribution.qos.observers.InvokerFailureRatioObserver;
import br.ufpe.cin.middleware.distribution.qos.observers.InvokerRequestsPerSecondObserver;
import br.ufpe.cin.middleware.distribution.qos.observers.InvokerResponseTimeObserver;
import br.ufpe.cin.middleware.distribution.qos.observers.RemoteObjectFailureRatioObserver;
import br.ufpe.cin.middleware.distribution.qos.observers.RemoteObjectRequestsPerSecondObserver;
import br.ufpe.cin.middleware.distribution.qos.observers.RemoteObjectResponseTimeObserver;
import br.ufpe.cin.middleware.distribution.qos.observers.SystemObserver;
import br.ufpe.cin.middleware.infrastructure.client.SenderQueue;
import br.ufpe.cin.middleware.infrastructure.server.ServerHandler;
import br.ufpe.cin.middleware.naming.Naming;
import br.ufpe.cin.middleware.naming.NamingService;
import br.ufpe.cin.middleware.services.LocalServiceRegistry;

public class Middleware {
	
	private ServerHandler serverHandler;
	private LocalServiceRegistry registry;
	private InvokerEventSourceFactory iosf;
	private RemoteObjectEventSourceFactory roesf;
	private SystemObserver sysObserver;
	private Naming naming;
	private NamingService namingService;
	private ExecutorService namingExecutor;
	private ExecutorService serverExecutor;
	private MiddlewareConfig config;
	private static Middleware INSTANCE;
	
	public synchronized static Middleware createInstance(MiddlewareConfig config)
	{
		if(INSTANCE == null)
		{
			INSTANCE = new Middleware(config);
		}
		return INSTANCE;
	}
	
	public static Middleware getInstance()
	{
		return INSTANCE;
	}
	
	private Middleware()
	{
		initializeConfigs();
		
		initializeServiceRegistry();
		initializeEventSourceFactories();
	
		initializeServices();
		
		initializeExecutors();
	}
	
	private Middleware(MiddlewareConfig config)
	{
		this.config = config;
		initializeConfigs();
		
		initializeServiceRegistry();
		initializeEventSourceFactories();
	
		initializeServices();
	}

	public void initializeExecutors()
	{
		if(namingService != null)
		{
			namingExecutor = Executors.newSingleThreadExecutor();
			namingExecutor.execute(namingService);
		}
		if(serverHandler != null)
		{
			serverExecutor = Executors.newSingleThreadExecutor();
			serverExecutor.execute(serverHandler);
		}
		
	}
	
	public void closeExecutors()
	{
		if(namingExecutor != null)
			namingExecutor.shutdown();
		if(serverExecutor != null)
			serverExecutor.shutdown();
		
		SenderQueue.getInstance().close();
	}
	
	private void initializeConfigs()
	{
		if(config == null)
		{
			config = new MiddlewareConfig();
			
			config.setProperty(MiddlewareConfig.NAMING_ENABLED, new Boolean(true).toString());
			config.setProperty(MiddlewareConfig.NAMING_SERVICE_HOST, "127.0.0.1");
			config.setProperty(MiddlewareConfig.NAMING_SERVICE_PORT, new Integer(NamingService.PORT).toString());
			config.setProperty(MiddlewareConfig.SERVER_PORT, new Integer(15001).toString());
			config.setProperty(MiddlewareConfig.SERVER_ENABLED, new Boolean(true).toString());
			
			config.setProperty(MiddlewareConfig.INVOKER_OBSERVER_RTM_ENABLED, "true");
			config.setProperty(MiddlewareConfig.INVOKER_OBSERVER_RPS_ENABLED, "true");
			config.setProperty(MiddlewareConfig.INVOKER_OBSERVER_FR_ENABLED, "true");
			
			config.setProperty(MiddlewareConfig.REMOTE_OBJECT_OBSERVER_RTM_ENABLED, "true");
			config.setProperty(MiddlewareConfig.REMOTE_OBJECT_OBSERVER_RPS_ENABLED, "true");
			config.setProperty(MiddlewareConfig.REMOTE_OBJECT_OBSERVER_FR_ENABLED, "true");
			
			config.setProperty(MiddlewareConfig.SYSTEM_OBSERVER_ENABLED, "true");
			
		}
		
	}
	
	private void initializeServices()
	{
		Boolean namingEnabled = Boolean.parseBoolean((String)config.getProperty(MiddlewareConfig.NAMING_ENABLED));
		
		String namingPortStr = config.getProperty(MiddlewareConfig.NAMING_SERVICE_PORT);
		Integer namingPort = Integer.parseInt(namingPortStr == null ? ""+NamingService.PORT : namingPortStr);
		
		String namingHost = config.getProperty(MiddlewareConfig.NAMING_SERVICE_HOST);
		
		String serverPortStr = config.getProperty(MiddlewareConfig.SERVER_PORT);
		Integer serverPort = Integer.parseInt(serverPortStr == null ? ""+ServerHandler.PORT : serverPortStr);
		Boolean serverEnabled = Boolean.TRUE.toString().equals(""+config.getProperty(MiddlewareConfig.SERVER_ENABLED));
		
		if(namingEnabled != null && namingEnabled)
		{
			namingHost = "127.0.0.1";
			this.namingService = new NamingService(namingPort);
		}
		
		if(serverEnabled != null && serverEnabled)
		{
			this.serverHandler = new ServerHandler(serverPort, namingHost, namingPort);
			this.serverHandler.setServiceRegistry(registry);
		}
		
		
		this.naming = new Naming(namingHost, namingPort);
		
	}

	/**
	 * 	Define Services Here, creating LocalServiceRegistry manually or 
	 * 	redefining the createDefault method 
	 */	
	private void initializeServiceRegistry() {
		registry = LocalServiceRegistry.createDefault();
		
	}

	public void initializeEventSourceFactories()
	{
		
		iosf = InvokerEventSourceFactory.getInstance();
		roesf = RemoteObjectEventSourceFactory.getInstance();
		
		if(Boolean.TRUE.toString().equals("" + config.getProperty(MiddlewareConfig.SERVER_ENABLED)))
		{
			boolean monEnabled = Boolean.TRUE.toString().equals(
					config.getProperty(""+MiddlewareConfig.MONITORING_ENABLED));
			
			if(monEnabled)
			{
				iosf.setEnabled(true);
				roesf.setEnabled(true);
				
				boolean irtm = Boolean.TRUE.toString().equals(
						config.getProperty(""+MiddlewareConfig.INVOKER_OBSERVER_RTM_ENABLED)); 
				boolean irps = Boolean.TRUE.toString().equals(
						config.getProperty(""+MiddlewareConfig.INVOKER_OBSERVER_RPS_ENABLED));
				boolean ifr = Boolean.TRUE.toString().equals(
						config.getProperty(""+MiddlewareConfig.INVOKER_OBSERVER_FR_ENABLED));
				
				boolean rortm = Boolean.TRUE.toString().equals(
						config.getProperty(""+MiddlewareConfig.REMOTE_OBJECT_OBSERVER_RTM_ENABLED)); 
				boolean rorps = Boolean.TRUE.toString().equals(
						config.getProperty(""+MiddlewareConfig.REMOTE_OBJECT_OBSERVER_RPS_ENABLED));
				boolean rofr = Boolean.TRUE.toString().equals(
						config.getProperty(""+MiddlewareConfig.REMOTE_OBJECT_OBSERVER_FR_ENABLED));
				
				boolean sys = Boolean.TRUE.toString().equals(
						config.getProperty(""+MiddlewareConfig.SYSTEM_OBSERVER_ENABLED));
				
				if(irps)
					iosf.registerObserver(new InvokerRequestsPerSecondObserver());
				if(irtm)
					iosf.registerObserver(new InvokerResponseTimeObserver());
				if(ifr)
					iosf.registerObserver(new InvokerFailureRatioObserver());
				
				if(rortm)
					roesf.registerObserver(new RemoteObjectResponseTimeObserver());
				if(rorps)
					roesf.registerObserver(new RemoteObjectRequestsPerSecondObserver());
				if(rofr)
					roesf.registerObserver(new RemoteObjectFailureRatioObserver());
				
				if(sys)
					sysObserver = new SystemObserver();
			}
			else
			{
				iosf.setEnabled(false);
				roesf.setEnabled(false);
			}
		}
	}

	public void startServersAndLoop()
	{
		try {
			initializeExecutors();
			
			while(true)
			{
				Thread.sleep(10000000);
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			this.serverExecutor.shutdownNow();
			this.namingExecutor.shutdownNow();
		}
	}

	public LocalServiceRegistry getRegistry() {
		return registry;
	}

	public InvokerEventSourceFactory getIosf() {
		return iosf;
	}

	public Naming getNaming() {
		return naming;
	}

	public MiddlewareConfig getConfig() {
		return config;
	}
	
	
	
	public static void main(String[] args) {
		
//		FileOutputStream fos = null;
//		try
//		{
//			fos = new FileOutputStream("/home/diego/middleware.properties");
//			m.getConfig().getConfig().store(fos, "");
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			if(fos != null)
//			{
//				try {
//					fos.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		
		MiddlewareConfig config = null;
		
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("-file"))
			{
				String configFileStr = (i+1) < args.length ? args[i+1] : null;
				if(configFileStr != null)
				{
					File configFile = new File(configFileStr);
					try {
						configFile.createNewFile();						
						config = new MiddlewareConfig();
						config.readFromFile(configFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		Middleware m = Middleware.createInstance(config);
		
		m.startServersAndLoop();
	}
	
	
	
}
