package br.ufpe.cin.middleware.distribution.cloud;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class CloudProxy {
	private ExecutorService executor;
	
	private ServerSocket server	= null;
	PropertiesSetup props		= new PropertiesSetup("/service.properties");
	private int port 			= Integer.parseInt(props.getProperties().getProperty("cloudproxy_port"));

	public CloudProxy(){
		try {
			this.server		= new ServerSocket(this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.executor 	= Executors.newCachedThreadPool();
	}
	public void run(){
		System.out.println("CloudProxy listening on " + this.port);
		while(true){
			try {
				Socket socket = this.server.accept();
				CloudProxyTask task = new CloudProxyTask(socket, props);
				executor.execute(task);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
