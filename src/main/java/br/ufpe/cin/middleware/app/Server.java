package br.ufpe.cin.middleware.app;


import br.ufpe.cin.middleware.infrastructure.server.ServerHandler;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PropertiesSetup properties	= new PropertiesSetup("/container.properties");
		ServerHandler server		= new ServerHandler(properties);
		server.run();
	}
}
