package br.ufpe.cin.in1118.application.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.infrastructure.server.ServerRequestHandler;
import br.ufpe.cin.in1118.management.monitoring.Agent;
import br.ufpe.cin.in1118.management.monitoring.EventSourceFactory;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.services.commons.naming.LocalServiceRegistry;
import br.ufpe.cin.in1118.services.commons.naming.Naming;
import br.ufpe.cin.in1118.utils.Network;
import br.ufpe.cin.in1118.utils.PropertiesSetup;

public class Broker {

	private ServerSocket 				server			= null;
	private ServerRequestHandler		srh				= ServerRequestHandler.getInstance();
	private String						serverHost		= "localhost";
	private int							serverPort		= 1313;
	private static PropertiesSetup		systemProps		= null;
	private static LocalServiceRegistry	registry		= null;
	private static String 				ROLE			= "application";
	private static NamingStub			naming			= null;
	private NodeManager					nodeManager		= null;
	private boolean						managerEnabled	= false;
	private boolean						feEnabled		= false;
	private String 						feHost			= null;
	private int							fePort			= 1212;

	private static ExecutorService	executor		= Executors.newFixedThreadPool(256);

	public Broker(String props){
		Broker.systemProps	= new PropertiesSetup(props);
		this.serverHost		= Network.recoverAddress("localhost");
		this.serverPort		= Integer.parseInt((String)Broker.systemProps.getProperties().get("port_number"));
		ROLE				= (String) Broker.systemProps.getProperties().get("role");

		if(ROLE.equals("frontend")){
			
			String host		= (String) Broker.systemProps.getProperties().get("naming_host");
			int	portNumber	= Integer.parseInt((String) Broker.systemProps.getProperties().get("naming_port"));
			this.feHost		= this.serverHost;
			this.fePort		= this.serverPort;

			Broker.registry	= LocalServiceRegistry.createDefault();
			Broker.naming	= new NamingStub(host, portNumber);

		} else if(ROLE.equals("registry")){
			String frontend = (String) Broker.systemProps.getProperties().get("frontend");
			this.feEnabled	= frontend.equals("enabled") ? true : false;
			this.feHost		= Network.recoverAddress((String) Broker.systemProps.getProperties().get("frontend_host"));
			this.fePort		= Integer.parseInt((String) Broker.systemProps.getProperties().get("frontend_port"));

			Broker.registry	= LocalServiceRegistry.createDefault();
			Broker.naming = new NamingStub(this.serverHost, this.serverPort);

		} else {
			String frontend = (String) Broker.systemProps.getProperties().get("frontend");
			this.feEnabled	= frontend.equals("enabled") ? true : false;
			this.feHost		= Network.recoverAddress((String) Broker.systemProps.getProperties().get("frontend_host"));
			this.fePort		= Integer.parseInt((String)	Broker.systemProps.getProperties().get("frontend_port"));

			String host		= (String) Broker.systemProps.getProperties().get("naming_host");
			int	portNumber	= Integer.parseInt((String) Broker.systemProps.getProperties().get("naming_port"));

			Broker.registry	= LocalServiceRegistry.createDefault();
			Broker.naming	= new NamingStub(host, portNumber);
		}

		// if (ROLE.equals("application") || ROLE.equals("registry")){//(Broker.systemProps.getProperties().containsKey("frontend")){
		// 	String frontend = (String) Broker.systemProps.getProperties().get("frontend");
		// 	this.feEnabled	= frontend.equals("enabled") ? true : false;
		// 	this.feHost		= Network.recoverAddress((String)
		// 			Broker.systemProps.getProperties().get("frontend_host"));
		// 	this.fePort		= Integer.parseInt((String) 
		// 			Broker.systemProps.getProperties().get("frontend_port"));
		// }

		// if(!ROLE.equals("registry")){
		// 	Broker.registry	= LocalServiceRegistry.createDefault();
		// 	String host		= (String) Broker.systemProps.getProperties().get("naming_host");
		// 	int	portNumber	= 
		// 			Integer.parseInt((String) Broker.systemProps.getProperties().get("naming_port"));

		// 	Broker.naming	= new NamingStub(host, portNumber);
		// }

		//Enabling management
		if(((String)Broker.systemProps.getProperties().get("system_monitor")).equals("enable")
				|| ((String)Broker.systemProps.getProperties().get("object_monitor")).equals("enable")){
			this.setManagerEnabled(true);
			String service = "NodeManagerService".toLowerCase() + '@' + Network.recoverAddress("localhost");
			try {
				Broker.registry.addService(service, Class.forName("br.ufpe.cin.in1118.management.node.NodeManagerService"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public static PropertiesSetup getSystemProps() {
		return Broker.systemProps;
	}

	public static void setSystemProps(PropertiesSetup systemProps) {
		Broker.systemProps = systemProps;
	}

	public NodeManager getNodeManager() {
		return nodeManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public static synchronized NamingStub getNaming(){
		return Broker.naming;
	}

	public boolean isManagerEnabled() {
		return this.managerEnabled;
	}

	public void setManagerEnabled(boolean managerEnabled) {
		this.managerEnabled = managerEnabled;
	}

	public static ExecutorService getExecutorInstance(){
		if(executor == null)
			executor = Executors.newFixedThreadPool(512);
		return executor;
	}

/* 	public LocalServiceRegistry getRegistry() {
		return registry;
	} */

	public void setRegistry(LocalServiceRegistry registry) {
		Broker.registry = registry;
	}

	public static String getROLE() {
		return ROLE;
	}

	public static void setROLE(String role) {
		ROLE = role;
	}

	public String getFeHost() {
		return feHost;
	}

	public void setFeHost(String feHost) {
		this.feHost = feHost;
	}

	public int getFePort() {
		return this.fePort;
	}

	public void setFePort(int fePort) {
		this.fePort = fePort;
	}

	public static void publishAllServices() {
		boolean	fe_en		= false;
		String	fe_host		= "localhost";
		int		fe_port		= 1313;
		String	serverHost	= "localhost";

		int serverPort = Integer.parseInt((String)Broker.systemProps.getProperties().get("port_number"));
		if(Broker.systemProps.getProperties().containsKey("frontend")){
			String frontend = (String) Broker.systemProps.getProperties().get("frontend");
			fe_en	= frontend.equals("enabled") ? true : false;
			fe_host	= Network.recoverAddress((String) Broker.systemProps.getProperties().get("frontend_host"));
			fe_port	= Integer.parseInt((String) Broker.systemProps.getProperties().get("frontend_port"));
		}

		//binding all services
		Set<String> serviceNames = Broker.registry.getAllServiceNames();

		for(String serviceName : serviceNames) {
			try {
				String className = Broker.registry.getRemoteObjectClass(serviceName).getSimpleName();
				Class<?> clazz = Broker.getStub(className);
				System.out.println("[Broker] Registering service "
						+ serviceName + ": " + clazz.getCanonicalName());
				Broker.publishService(serviceName,clazz, fe_en, fe_host, fe_port,serverHost, serverPort);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	public static LocalServiceRegistry getRegistry(){
		return Broker.registry;
	}
	public static Class<?> getStub(String className) throws ClassNotFoundException {
		return Class.forName("br.ufpe.cin.in1118.distribution.stub." + className + "Stub");
	}

	public static void publishService(String serviceName, Class<?> clazz, boolean fe_en, String fe_host, int fe_port, String serverHost, int serverPort){
		Stub stub = null;

		try {
			stub = (Stub) clazz.getDeclaredConstructor().newInstance();
			//stub = (Stub) clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		Method method = null;
		//First checks if services uses Frontend services before set host and 
		try {
			if(fe_en){
				method = clazz.getMethod("setFeHost", new Class[]{String.class});
				method.invoke(stub, fe_host);
				method = clazz.getMethod("setFePort", new Class[]{Integer.TYPE});
				method.invoke(stub, Integer.valueOf(fe_port));
				method = clazz.getMethod("setForwarded", new Class[]{Boolean.TYPE});
				method.invoke(stub, fe_en);
			}
			method = clazz.getMethod("setServiceName", new Class[]{String.class});
			method.invoke(stub, serviceName);

			method = clazz.getMethod("setHost", new Class[]{String.class});
			method.invoke(stub, serverHost);

			method = clazz.getMethod("setPort", new Class[]{Integer.TYPE});
			method.invoke(stub, Integer.valueOf(serverPort));

			method = clazz.getMethod("setObjectId", new Class[]{Integer.TYPE});
			method.invoke(stub, Integer.valueOf(1));

			method = clazz.getMethod("setClassName", new Class[]{String.class});
			method.invoke(stub, registry.getRemoteObjectClass(serviceName).getCanonicalName());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		Broker.naming.bind(serviceName, stub);
	}

	private void startManagement(){
		if(this.isManagerEnabled()){
			this.setNodeManager(NodeManager.getInstance());
			if(((String)Broker.systemProps.getProperties().get("system_monitor")).equals("enable"))
				this.getNodeManager().setSysMonitorEnabled(true);

			if(((String)Broker.systemProps.getProperties().get("object_monitor")).equals("enable")){
				this.getNodeManager().setObjectMonitorEnabled(true);
				this.nodeManager.getObjectMonitor().setAgents(EventSourceFactory.getInstance().getObservers());
			}

			//Registering observers for all services.
			if(Broker.ROLE.equals("registry"))
				EventSourceFactory.getInstance().registerObserver(Naming.class.getName(), new Agent());
			else {
				Set<String> services = Broker.registry.getAllServiceNames();	
				for(String str : services)
					EventSourceFactory.getInstance().registerObserver(Broker.registry.getRemoteObjectClass(str).getName(), new Agent(str));
			}

			//this.nodeManager.getObjectMonitor().setAgents(EventSourceFactory.getInstance().getObservers());
			this.getNodeManager().start();
		}
	}

	public void start () {
		//Starting the server
		try {	
			if(this.server == null){
				this.server = new ServerSocket(this.serverPort);
				System.out.println("[Broker] " + ROLE
						+ " server running and listening on port "
						+ this.serverPort);
			}

			this.startManagement();

			//Preparing to bind the remote objects. For registry server it isn't realized
			if(Broker.ROLE.equals("application")){				
				Broker.publishAllServices();

				/*				@SuppressWarnings("unchecked")
				Class<Stub> clazz = (Class<Stub>) Broker.getStub("NodeManagerService");
				LocalServiceRegistry.getINSTANCE().addService("NodeManagerService", clazz);
				Broker.publishService("NodeManagerService",
						clazz,
						feEnabled,
						feHost,
						fePort,
						serverHost,
						serverPort);
				 */
			}
			while(true)
				srh.receive(this.server.accept());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			this.endServer();
		}
	}

	public void endServer(){
		if(this.server != null && !this.server.isClosed())
			try {
				this.server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}