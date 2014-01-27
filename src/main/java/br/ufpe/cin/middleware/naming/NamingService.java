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

public class NamingService implements Runnable{
	
	public static final int PORT = 5000;
	private ServerSocket server = null;
	private Map<String, Set<Service>> map;
	private ExecutorService executor;
	private ReadWriteLock lock;
	
	public NamingService() {
		try {
			this.server		= new ServerSocket(NamingService.PORT);
			this.map 		= new HashMap<String, Set<Service>>();
			this.executor 	= Executors.newCachedThreadPool();
			this.lock 		= new ReentrantReadWriteLock();
			
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
	
	public void run(){
		System.out.println("Naming Service started on port " + NamingService.PORT);
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

/*	public Object processMessage(){
		
		Object obj = null;
		this.parse();
		
		if (this.source == 's'){
			System.out.println("Registering service ");
			if (this.operation == 'r'){
				if(!map.containsKey(this.serviceName)) {
					Object hosts =  this.map.get(this.serviceName);
					hosts = (hosts == null) ? new TreeSet<Service>() : hosts;
					hosts.add(this.host);
					this.map.put(this.serviceName, hosts);
				}
			} else if(this.operation == 'u') {
				this.map.remove(this.host);
			}
			
		} else if (this.source == 'c'){
			System.out.println("Client searching a service... ");
			
			obj = this.search(this.serviceName);
		}
		return obj;
	}*/
	
/*	private Object search(String sn) {
		Object o = null;
		
		Set set = map.entrySet();
		Iterator<Service> i = set.iterator();
		while(i.hasNext()){
			Map.Entry me = (Map.Entry)i.next();
			if (((Service)me.getValue()).getName().equals(this.serviceName)){
				o = me.getValue();
				break;
			}
		}
		return o; 
	}*/

/*	public void parse(){
		this.host = new Service();
		String[] split = this.incomingMessage.split(":");
		this.source = this.incomingMessage.charAt(0);
		
		if (this.source == 's'){
			this.host.setName(split[1]);
			this.host.setHost(split[2]);
			this.host.setPort(Integer.parseInt(split[3]));
			split[4].charAt(0);
			
		} else if(this.source == 'c'){
		}
	}*/
	
}