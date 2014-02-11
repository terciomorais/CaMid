package br.ufpe.cin.middleware.distribution.cloud;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class CloudProxy {
	
	private ExecutorService executor;	
	private ServerSocket server			= null;
	private PropertiesSetup properties	= null;
	private int port					= 5001;

	public CloudProxy(String props){
		this.setProperties(new PropertiesSetup(props));
		int port = Integer.parseInt(this.getProperties().getProperties().getProperty("cloudproxy_port"));
		this.setPort(port);

		try {
			this.server	= new ServerSocket(this.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.executor = Executors.newCachedThreadPool();
	}
	
	public CloudProxy(){
		try {
			this.server	= new ServerSocket(this.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.executor = Executors.newCachedThreadPool();
	}
	
	public PropertiesSetup getProperties() {
		return properties;
	}
	private void setProperties(PropertiesSetup props) {
		this.properties = props;
	}
	public int getPort() {
		return port;
	}
	private void setPort(int port) {
		this.port = port;
	}
	public void run(){
		System.out.println("CloudProxy listening on " + this.port);
		while(true){
			try {
				Socket socket = this.server.accept();
				CloudProxyTask task = new CloudProxyTask(socket, this.getProperties());
				executor.execute(task);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
