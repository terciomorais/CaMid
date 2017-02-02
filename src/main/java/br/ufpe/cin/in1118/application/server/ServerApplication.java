package br.ufpe.cin.in1118.application.server;

public class ServerApplication {
	public static void main(String[] args) {
		Broker broker = new Broker("config/camid.config");
		broker.start();
	}
}
