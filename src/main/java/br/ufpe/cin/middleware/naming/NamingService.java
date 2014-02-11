package br.ufpe.cin.middleware.naming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class NamingService implements Runnable{
	
	//public static final int PORT = 5000;
	private int port 						= 5000; //default value
	private ServerSocket server 			= null;
	private Map<String, Set<Service>> map;
	private ExecutorService executor;
	private ReadWriteLock lock;
	
	private PropertiesSetup properties		= null;
	
	public NamingService() {
		try {
			this.server		= new ServerSocket(this.port);
			this.map 		= new HashMap<String, Set<Service>>();
			this.executor 	= Executors.newCachedThreadPool();
			this.lock 		= new ReentrantReadWriteLock();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NamingService(String props){
		this.setProperties(new PropertiesSetup(props));
		
		this.port = Integer.parseInt(this.getProperties().getProperties().getProperty("naming_port"));

		try {
			this.server		= new ServerSocket(this.port);
			this.map		= new HashMap<String, Set<Service>>();
			this.executor	= Executors.newCachedThreadPool();
			this.lock		= new ReentrantReadWriteLock();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NamingService(int port) {
		try {
			this.server		= new ServerSocket(port);
			this.map		= new HashMap<String, Set<Service>>();
			this.executor	= Executors.newCachedThreadPool();
			this.lock		= new ReentrantReadWriteLock();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public PropertiesSetup getProperties() {
		return this.properties;
	}
	private void setProperties(PropertiesSetup props) {
		this.properties = props;
	}
	
	public void run(){
		System.out.println("Naming Service started on port " + this.port);
		while(true){
			this.clearAll();
			try {
				Socket socket = this.server.accept();
				NamingServiceTask task = new NamingServiceTask(socket, map, lock);
				executor.execute(task);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void clearAll() {
	}	
}