package br.ufpe.cin.in1118.infrastructure.server;

import java.net.Socket;
import br.ufpe.cin.in1118.application.server.Broker;

public class ServerRequestHandler {

	private static ServerRequestHandler INSTANCE = null;
	
	public static synchronized ServerRequestHandler getInstance(){
		if (INSTANCE == null)
			INSTANCE = new ServerRequestHandler();
		return INSTANCE;
	}
	
	public void receive(Socket conn){
		Broker.getExecutorInstance().execute(new Receiver(conn));
	}
}
