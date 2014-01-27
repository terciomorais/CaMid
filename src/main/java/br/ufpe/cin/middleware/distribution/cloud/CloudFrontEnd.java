package br.ufpe.cin.middleware.distribution.cloud;

import java.net.ServerSocket;

import br.ufpe.cin.middleware.naming.NamingService;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class CloudFrontEnd{
	
	private NamingService namingService	= null;
	private CloudProxy cloudProxy		= null;
	private PropertiesSetup ps			= new PropertiesSetup("/service.properties");
	private ServerSocket cloudServer	= null;
	
	public CloudFrontEnd(){
		//startup Naming service
		int port = Integer.parseInt(ps.getProperties().getProperty("naming_port"));
		this.namingService = new NamingService(port);
		this.namingService.run();
		
		this.cloudProxy	= new CloudProxy();
	}
	
	public CloudFrontEnd(String confFile){
		
		if(!confFile.startsWith("/"))
			confFile = "/" + confFile;
		
		int port 		= Integer.parseInt(ps.getProperties().getProperty(confFile));
		this.namingService	= new NamingService(port);
		this.namingService.run();
		
		port 		= Integer.parseInt(ps.getProperties().getProperty(confFile));
		cloudProxy 	= new CloudProxy();
	}
}
