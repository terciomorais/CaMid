package br.ufpe.cin.middleware.app;


import br.ufpe.cin.middleware.infrastructure.server.ServerHandler;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class Server2Cloud1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PropertiesSetup properties	= new PropertiesSetup("/Cloud1Container2.properties");
		ServerHandler server		= new ServerHandler(properties); 

		server.run();
	}
}
