package br.ufpe.cin.in1118.application.server;

public class NameServer {

	public static void main(String[] args) {
		Broker broker	= new Broker("config/registry.config");
		broker.start();
	}
}
