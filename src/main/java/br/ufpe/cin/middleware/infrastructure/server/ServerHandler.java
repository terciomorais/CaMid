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
import br.ufpe.cin.middleware.utils.Network;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class ServerHandler implements Runnable{
	
	private ServerSocket server 			= null;
	private Linker binding 					= null;//For registry use
	private ExecutorService executor		= Executors.newCachedThreadPool();
	
	private String incomingMessage 			= "";
	private LocalServiceRegistry registry	= null;

	private String containerEndPoint		= "";
	private int containerPort;
	private String namingEndPoint			= "";
	private int namingPort; 
	
	public ServerHandler(int port) {
		this.setContainerPort(port);
		this.binding = new Linker();
		this.registry = LocalServiceRegistry.createDefault();
	}
	
	public ServerHandler(int namingPort, String namingHost)	{
		this.setNamingEndPoint(namingHost);
		this.setNamingPort(namingPort);
		
		this.binding = new Linker(this.getNamingEndPoint(), this.getNamingPort());
		this.registry = LocalServiceRegistry.createDefault();
	}
	
	public ServerHandler(int containerPort, String namingHost, int namingPort) {
		this.setContainerPort(containerPort);
		this.setNamingEndPoint(namingHost);
		this.setNamingPort(namingPort);
		
		this.binding = new Linker(this.getNamingEndPoint(), this.getNamingPort());
		this.registry = LocalServiceRegistry.createDefault();
	}

	public ServerHandler(PropertiesSetup properties) {
		
		this.setContainerPort(Integer.parseInt(properties.getProperties().getProperty("container_port")));
		this.setContainerEndPoint(properties.getProperties().getProperty("container_endpoint"));
		
		this.setNamingPort(Integer.parseInt(properties.getProperties().getProperty("naming_port")));
		this.setNamingEndPoint(properties.getProperties().getProperty("naming_endpoint"));
		
		this.binding = new Linker(this.getNamingEndPoint(), this.getNamingPort());
		this.registry = LocalServiceRegistry.createDefault();
	}

	public int getContainerPort() {
		return containerPort;
	}

	public void setContainerPort(int containerPort) {
		this.containerPort = containerPort;
	}

	public String getContainerEndPoint() {
		return containerEndPoint;
	}

	public void setContainerEndPoint(String containerEndPoint) {
		this.containerEndPoint = Network.recoverAddress(containerEndPoint);
	}

	public int getNamingPort() {
		return namingPort;
	}

	public void setNamingPort(int namingPort) {
		this.namingPort = namingPort;
	}

	public String getNamingEndPoint() {
		return namingEndPoint;
	}

	public void setNamingEndPoint(String namingEndPoint) {
		this.containerEndPoint = Network.recoverAddress(namingEndPoint);
	}

	public Linker getBinding() {
		return binding;
	}

	public void run(){
		
		//Registering the service at naming service
		try {
			server = new ServerSocket(this.containerPort);
			System.out.println("Server started and listening at " + InetAddress.getByName("localhost").getHostAddress() +
					":" + this.getContainerPort());
			this.publishAllServices();
			//this.binding.bind(
				//	new Service(this.getServiceName(), InetAddress.getByName("localhost").getHostAddress(), this.getContainerPort()));
			while(true){
				try {
					executor.submit(new Receiver(this.server.accept(), this.registry));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally{
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
			Service s = new Service(serviceName, this.getContainerPort());
			this.binding.bind(s);
		}
	}
}
