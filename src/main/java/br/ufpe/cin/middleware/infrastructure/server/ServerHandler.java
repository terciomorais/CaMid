package br.ufpe.cin.middleware.infrastructure.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.middleware.naming.Linker;
import br.ufpe.cin.middleware.naming.Service;
import br.ufpe.cin.middleware.services.LocalServiceRegistry;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class ServerHandler implements Runnable{
	
	private ServerSocket server 			= null;
	private Linker binding 					= null;//For registry use
	private ExecutorService executor		= Executors.newCachedThreadPool();
	
	private String incomingMessage 			= "";
	private LocalServiceRegistry registry;
	
	private String serviceName 				= "";
	private int servicePort;
	
	private String namingEndPoint			= "";
	private int namingPort;
	public static final int PORT 			= 15001;
	
	public ServerHandler(int port) {
		this.servicePort = port;
		this.binding = new Linker();
	}
	
	public ServerHandler(int serverPort, String namingHost)	{
		this.servicePort = serverPort;
		this.binding = new Linker(namingHost, serverPort);
	}
	
	public ServerHandler(int serverPort, String namingHost, int namingPort) {
		this.servicePort = serverPort;
		this.binding = new Linker(namingHost, namingPort);
	}
	
	public ServerHandler(String serviceName, int servicePort, String namingEndPoint, int namingPort){
		this.setServiceName(serviceName);
		this.setServicePort(servicePort);
		this.setNamingEndPoint(namingEndPoint);
		this.setNamingPort(namingPort);
	}

	public ServerHandler(PropertiesSetup properties) {
		this.setServiceName(properties.getProperties().getProperty("service_name"));
		System.out.println(properties.getProperties().getProperty("service_port"));
		this.setServicePort(Integer.parseInt(properties.getProperties().getProperty("service_port")));
		this.setNamingEndPoint(properties.getProperties().getProperty("naming_endpoint"));
		this.setNamingPort(Integer.parseInt(properties.getProperties().getProperty("naming_port")));
		this.binding = new Linker(this.namingEndPoint, this.namingPort);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	public String getNamingEndPoint() {
		return namingEndPoint;
	}

	public void setNamingEndPoint(String namingEndPoint) {
		this.namingEndPoint = namingEndPoint;
	}

	public int getNamingPort() {
		return namingPort;
	}

	public void setNamingPort(int namingPort) {
		this.namingPort = namingPort;
	}

	public Linker getBinding() {
		return binding;
	}

	public void run(){

		//Registering the service at naming service
		try {
			server = new ServerSocket(this.servicePort);
			System.out.println("Server started and listening at " + InetAddress.getByName("localhost").getHostAddress() +
					":" + this.getServicePort());
			
			this.binding.bind(
					new Service(this.getServiceName(), InetAddress.getByName("localhost").getHostAddress(), this.getServicePort()));
//			this.binding.bind(new Service("LocalAgent", InetAddress.getByName("localhost").getHostAddress(), this.getServicePort()));
			
//Starting the service to wait for client requests. For each received request, a new thread is launched.
			while(true){
				try {
					//this.receivers.add(new Receiver(this.server.accept()));
					executor.submit(new Receiver(this.server.accept(), registry));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} 
		catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally
		{
			try {
				this.server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getIncomingMessage() {
		return incomingMessage;
	}

	public void setIncomingMessage(String incomingMessage) {
		this.incomingMessage = incomingMessage;
	}
	
	public void setServiceRegistry(LocalServiceRegistry registry) {
		this.registry = registry;
	}
	
	public LocalServiceRegistry getServiceRegistry() {
		return this.registry;
	}
	
	public void publishAllServices() throws UnknownHostException {
		Set<String> serviceNames = this.registry.getAllServices();
		for(String serviceName : serviceNames) {
			Service s = new Service(serviceName, ServerHandler.PORT);
			System.out.println("Service added: " + serviceName);
			this.binding.bind(s);
		}
	}
}
