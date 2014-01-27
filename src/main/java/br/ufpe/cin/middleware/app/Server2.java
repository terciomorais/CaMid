package br.ufpe.cin.middleware.app;


import br.ufpe.cin.middleware.infrastructure.server.ServerHandler;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class Server2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PropertiesSetup properties	= new PropertiesSetup("service1.properties");
		ServerHandler server		= new ServerHandler(properties); 

		server.run();
	}
}
